package org.tyniest.websocket.tokenStore;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.tyniest.websocket.state.SessionState;
import org.tyniest.websocket.tokenStore.RedisTokenStore.DeadSessionEleminationContext;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.hash.ReactiveHashCommands;
import io.quarkus.redis.datasource.hash.ReactiveTransactionalHashCommands;
import io.quarkus.redis.datasource.transactions.ReactiveTransactionalRedisDataSource;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.smallrye.mutiny.Uni;
import lombok.Value;
import lombok.experimental.Accessors;


/** Implemntation of the ReactiveToken store on top of redis for distributed session handling */
public class RedisTokenStore implements ReactiveSessionStateStore {

    private final int timeoutBeforeSessionFlush = 130;

    private final String userIdKey;
    private final String tokenKey;
    private final String stateKey;
    private final String instanceCountKey;

    private final String liveSessionKeyPrefix;

    /** userId -> token */
    private final ReactiveHashCommands<String, UUID, String> valueIdCommands;

    /** userId -> sessonState */
    private final ReactiveHashCommands<String, UUID, SessionState> sessionStates;

    /** token -> state */
    private final ReactiveHashCommands<String, String, SessionState> valueCommands;

    /** Counter to count how many instances of this instance are up and running */
    private final ReactiveValueCommands<String, Long> instanceCount;

    private final ReactiveValueCommands<String, String> stringCommands;

    private final ReactiveRedisDataSource ds;

    public RedisTokenStore(final ReactiveRedisDataSource ds, final RedisConfig config) {
        this.ds = ds;
        this.stringCommands = ds.value(String.class);
        this.valueIdCommands = makeValueIdCommands(ds);
        this.valueCommands = makeValueCommands(ds);
        this.sessionStates = makeSessionCommands(ds);
        this.instanceCount = ds.value(Long.class);

        // configure tokens
        this.userIdKey = config.userId();
        this.instanceCountKey = config.instanceCount();
        this.stateKey = config.state();
        this.tokenKey = config.token();
        this.liveSessionKeyPrefix = "wsSession";
    }

    /** In order to count how many instances are up */
    @PostConstruct
    protected void handleConnected() {
        this.instanceCount.incr(instanceCountKey).await().indefinitely();
    }

    /** In order to count how many instances are up */
    @PreDestroy
    public void handleDisconnected() {
        this.instanceCount.decr(instanceCountKey).await().indefinitely();
    }

    protected ReactiveHashCommands<String, UUID, SessionState> makeSessionCommands(
            final ReactiveRedisDataSource ds) {
        return ds.hash(String.class, UUID.class, SessionState.class);
    }

    protected ReactiveHashCommands<String, String, SessionState> makeValueCommands(
            final ReactiveRedisDataSource ds) {
        return ds.hash(SessionState.class);
    }

    protected ReactiveHashCommands<String, UUID, String> makeValueIdCommands(
            final ReactiveRedisDataSource ds) {
        return ds.hash(String.class, UUID.class, String.class);
    }

    protected ReactiveTransactionalHashCommands<String, UUID, SessionState> makeSessionCommands(
            final ReactiveTransactionalRedisDataSource ds) {
        return ds.hash(String.class, UUID.class, SessionState.class);
    }

    protected ReactiveTransactionalHashCommands<String, String, SessionState> makeValueCommands(
            final ReactiveTransactionalRedisDataSource ds) {
        return ds.hash(SessionState.class);
    }

    protected ReactiveTransactionalHashCommands<String, UUID, String> makeValueIdCommands(
            final ReactiveTransactionalRedisDataSource ds) {
        return ds.hash(String.class, UUID.class, String.class);
    }

    public Uni<SessionState> getStateByToken(final String token) {
        return this.valueCommands.hget(tokenKey, token);
    }

    public Uni<Void> deleteTokenIfExistsForId(final UUID userId) {
        return this.valueIdCommands
                .hget(userIdKey, userId)
                .flatMap(
                        token -> {
                            if (token != null) {
                                return this.deleteToken(token, userId);
                            }
                            return Uni.createFrom().voidItem();
                        });
    }

    public Uni<Void> putToken(final String token, final UUID userId, final SessionState state) {
        return ds.withTransaction(
                        tx -> {
                            final var tokenCommands = makeValueCommands(tx);
                            final var idCommands = makeValueIdCommands(tx);
                            return tokenCommands
                                    .hset(tokenKey, token, state)
                                    .chain(() -> idCommands.hset(userIdKey, userId, token));
                        })
                .replaceWithVoid();
    }

    public Uni<Void> deleteToken(final String token, final UUID userId) {
        return ds.withTransaction(
                        tx -> {
                            final var tokenCommands = makeValueCommands(tx);
                            final var idCommands = makeValueIdCommands(tx);
                            return tokenCommands
                                    .hdel(tokenKey, token)
                                    .chain(() -> idCommands.hdel(userIdKey, userId));
                        })
                .replaceWithVoid();
    }

    @Override
    public Uni<Void> putState(final UUID userId, final SessionState state) {
        return this.sessionStates.hset(stateKey, userId, state).replaceWithVoid();
    }

    @Override
    public Uni<Boolean> removeState(final UUID userId) {
        return this.sessionStates.hdel(stateKey, userId).map(i -> i.equals(1));
    }

    @Override
    public Uni<List<SessionState>> getAllStates() {
        return this.sessionStates.hvals(stateKey);
    }

    @Override
    public Uni<Boolean> removeStates(final UUID[] ids) {
        return this.sessionStates.hdel(stateKey, ids).map(i -> i.equals(ids.length));
    }

    @Override
    public Uni<Void> clearAll() {
        return this.sessionStates
                .hkeys(stateKey)
                .chain(keys -> this.sessionStates.hdel(stateKey, keys.toArray(UUID[]::new)))
                .replaceWithVoid();
    }

    protected String makeLiveSessionKey(final UUID id) {
        return liveSessionKeyPrefix + id;
    }

    protected Uni<Void> keepUserAlive(final UUID id) {
        return this.stringCommands.setex(
                makeLiveSessionKey(id),
                timeoutBeforeSessionFlush,
                "" // don't need a real value here, we just need the key to handle the timeout
                );
    }

    @Override
    public Uni<Void> keepUsersAlive(final List<UUID> ids) {
        if (ids.isEmpty()) {
            return Uni.createFrom().nothing();
        }
        // token+userId -> avec expire // permet de ne pas gérer la suppression des
        // clefs localement et de ne pas avoir de pb avec les dates d'éxpiration
        // trouve les clefs existantes par token+userId dans le sessionState
        // supprime celles pas trouvées de map
        return Uni.combine()
                .all()
                .unis(
                        ids.stream()
                                .map(this::keepUserAlive) // we can do this, because the
                                // client is pipelined by default
                                .collect(Collectors.toList()))
                .discardItems();
    }

    @Accessors(fluent = true)
    @Value(staticConstructor = "of")
    protected static class DeadSessionEleminationContext {
        protected List<UUID> expectedIds;
        protected Set<String> queryResult;
    }

    // avoid recreating the item
    protected final DeadSessionEleminationContext emptyContext =
            DeadSessionEleminationContext.of(List.of(), Set.of());

    @Override
    public Uni<Void> clearDeadSessions() {
        return this.getAllStates() // get all stored states
                .chain(
                        states -> {
                            if (states.isEmpty()) {
                                // TODO: not pretty, would be better if we could stop execution here
                                // directly
                                return Uni.createFrom().item(emptyContext);
                            }
                            final var ids =
                                    states.stream()
                                            .map(i -> i.getUserId())
                                            .collect(Collectors.toList());
                            return this.stringCommands // get liveSessionKey names
                                    .mget(
                                            ids.stream()
                                                    .map(this::makeLiveSessionKey)
                                                    .toArray(String[]::new))
                                    .map(
                                            res ->
                                                    DeadSessionEleminationContext.of(
                                                            ids, res.keySet()));
                        })
                .map(
                        res ->
                                res.expectedIds().stream() // filter to find wich wasn't deleted
                                        .filter(
                                                id ->
                                                        !res.queryResult()
                                                                .contains(
                                                                        this.makeLiveSessionKey(
                                                                                id))))
                .chain(
                        res -> {
                            final var arr = res.toArray(UUID[]::new);
                            if (arr.length > 0) {
                                return this.removeStates(arr).replaceWithVoid(); // delete them
                            } else {
                                return Uni.createFrom().nothing();
                            }
                        });
    }

    @Override
    public String getName() {
        return "redis";
    }

    @Override
    public boolean isLocal() {
        return false;
    }

    @Override
    public Uni<Long> countSessions() {
        return this.sessionStates.hlen(stateKey);
    }
}
