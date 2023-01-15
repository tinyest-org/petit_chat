package org.tyniest.websocket;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;
import javax.websocket.Session;

import org.tyniest.utils.UuidHelper;
import org.tyniest.utils.IPGeocoder.IPGeocoder;
import org.tyniest.utils.LockProvider.LockProvider;
import org.tyniest.utils.notifier.ChannelHandle;
import org.tyniest.utils.notifier.ExistingConflictingChannel;
import org.tyniest.utils.notifier.NotifierService;
import org.tyniest.websocket.state.SessionState;
import org.tyniest.websocket.tokenStore.ReactiveSessionStateStore;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import lombok.extern.slf4j.Slf4j;

/** A Singleton to hold auth details in order to bind a given user to a websocket connection */
@Singleton
@Slf4j
@Startup
public class ConnectionHolder implements Consumer<DisconnectUserNotification> {

    private final IPGeocoder ipGeocoder;
    private final LockProvider lockProvider;

    public static final String WS_NOTIFICATION_CHANNEL_NAME = "ws_disconnect";

    /** Local */
    private final ConcurrentHashMap<Session, SessionState> sessionToUser = new ConcurrentHashMap<>();

    /** Local */
    private final ConcurrentHashMap<UUID, Session> userIdToSession = new ConcurrentHashMap<>();

    /** Can be local */
    private final ConcurrentHashMap<UUID, SessionState> sessionIdToSessions = new ConcurrentHashMap<>();

    private final ReactiveSessionStateStore sessionStateStore;

    private final ChannelHandle<DisconnectUserNotification> handle;

    /** We keep a reference to this gauge in order to be sure not get garbage collected */
    private final Gauge allConnectedWsUsers;

    /** We keep a reference to this gauge in order to be sure not get garbage collected */
    private final Gauge localConnectedUsersGauge;

    public ConnectionHolder(
            final ReactiveSessionStateStore tokenStore,
            final IPGeocoder ipGeocoder,
            final LockProvider lockProvider,
            final NotifierService notifierService,
            final MeterRegistry registry) throws ExistingConflictingChannel {
        this.sessionStateStore = tokenStore;
        this.handle = notifierService
            .getHandle(WS_NOTIFICATION_CHANNEL_NAME, DisconnectUserNotification.class)
            .subscribe(this);
        this.ipGeocoder = ipGeocoder;
        this.lockProvider = lockProvider;
        this.allConnectedWsUsers =
                Gauge.builder("allConnectedWsUsers", this::countAllUsers).register(registry);
        this.localConnectedUsersGauge =
                Gauge.builder("localConnectedWsUsers", this::countLocalUsers).register(registry);
    }

    public Optional<SessionState> getSessionState(final UUID sessionId) {
        return Optional.ofNullable(this.sessionIdToSessions.get(sessionId));
    }

    /**
     * Binds a websocket connection to a KiwiUser
     *
     * @param connection
     * @param user
     * @return Uni<Void> with an on open websocket message
     */
    public Uni<Void> connectUser(
            final String token,
            final Session connection,
            final SessionState state,
            final Optional<String> ip) {
        return this.publishDisconnection(connection, state)
                .chain(
                        () -> {
                            state.setOpen();
                            ip.ifPresent(i -> state.setIp(i));
                            // careful properly handle deallocation
                            this.sessionIdToSessions.put(state.getId(), state);
                            this.sessionToUser.put(connection, state);
                            return this.sessionStateStore
                                    .putState(state.getUserId(), state)
                                    .chain(
                                            () ->
                                                    sessionStateStore.deleteToken(
                                                            token, state.getUserId()))
                                    .chain(
                                            () ->
                                                    Uni.createFrom()
                                                            .future(
                                                                    connection
                                                                            .getAsyncRemote()
                                                                            .sendText(
                                                                                    "{\"state\":"
                                                                                        + " true}")))
                                    .chain(
                                            () ->
                                                    this.updateGeocodingDefered(
                                                                    state.getUserId(),
                                                                    state) // try map location
                                                            .onFailure()
                                                            .recoverWithNull());
                        });
    }

    protected Uni<Void> updateGeocodingDefered(final UUID userId, final SessionState state) {
        final var ip = state.getIp();
        if (ip == null) {
            return Uni.createFrom().nothing();
        }
        return this.ipGeocoder
                .getLatLong(ip)
                .onItem()
                .delayIt()
                .by(Duration.ofSeconds(1))
                .chain(
                        res -> {
                            state.setLatitude(res.getLatitude());
                            state.setLongitude(res.getLongitude());
                            return this.sessionStateStore.putState(userId, state);
                        });
    }

    /**
     * @return SessionState if the token is bound to a valid user else null
     */
    public Uni<SessionState> getUserByToken(final String token) {
        return sessionStateStore.getStateByToken(token);
    }

    /**
     * Publishes a Message telling the other instances that the user connected itself to <b>this</b>
     * instance and that they should disconnect the user if he already has connected to them
     */
    protected Uni<Void> publishDisconnection(final Session connection, final SessionState state) {
        return handle.reactivePublish(
                new DisconnectUserNotification(connection.getId(), state.getUserId(), false)).replaceWithVoid();
    }

    /**
     * Returns the user associated to the given websocket connection
     *
     * @param connection a given websocket connection associated to a KiwiUser
     * @return Should never return null as a connection can only remain open if associated to a user
     */
    public Optional<SessionState> getSession(final Session connection) {
        return Optional.ofNullable(this.sessionToUser.get(connection));
    }

    /**
     * Disconnects a given user
     *
     * @param cutConnection if true, will close the websocket connection, else will simply remove
     *     the binding to the user
     */
    public SessionState disconnectSession(final Session connection, final boolean cutConnection) {
        log.debug("disconnected user: {}", connection.getId());
        if (cutConnection) {
            try {
                connection.close();
            } catch (IOException e) {
                log.error("failed to close");
            }
        }
        final var state = this.sessionToUser.remove(connection);
        this.sessionIdToSessions.remove(state.getId());
        this.sessionStateStore
                .removeState(state.getUserId())
                .subscribe()
                .with(
                        s -> {},
                        err -> {
                            log.error("err: {}", err);
                        }); // subscribe but don't block
        return state;
    }

    protected SessionState createState(
            final UUID userId, final Optional<String> ip, final Set<String> roles) {
        final var id = UUID.randomUUID();
        return SessionState.of(userId, id, ip.orElse(null), roles);
    }

    /** Makes a token in order to prepare a Websocket connection */
    protected String makeToken() {
        return UuidHelper.getCompactUUID4();
    }

    /**
     * Makes a token wich can be used by a given user in order to open a websocket connection
     *
     * @param user
     * @return
     */
    public Uni<String> prepareConnectionFor(
            final UUID userId, final Optional<String> ip, final Set<String> roles) {
        final var token = makeToken();
        final var state = createState(userId, ip, roles);
        return sessionStateStore
                .deleteTokenIfExistsForId(userId)
                .chain(() -> sessionStateStore.putToken(token, userId, state).replaceWith(token));
    }

    /** Only returns the session connected to this instance for now */
    public Uni<List<SessionState>> getConnectedUsers() {
        return this.sessionStateStore.getAllStates();
    }

    /** Handle distributed session disconnection */
    @Override
    public void accept(final DisconnectUserNotification message) {
        log.debug("Got message: {}", message);

        if (message.all) { // disconnecting all local users
            userIdToSession.values().forEach(c -> this.disconnectSession(c, true));
            return;
        }

        final var connection = userIdToSession.get(message.userId);
        if (connection != null // we have the connection locally
                && !connection
                        .getId()
                        .equals(message.openedSessionId) // if we just opened it do nothing
        ) {

            this.disconnectSession(connection, true); // else disconnect to corresponding user
        }
        // we have nothing to do
    }

    public Uni<Void> removeAllSessionState() {
        return this.sessionStateStore.clearAll();
    }

    /**
     * Disconnects all opened session accross all instances
     *
     * @return
     */
    public Uni<Void> disconnectAll() {
        return handle.reactivePublish(
                new DisconnectUserNotification("", null, true)).replaceWithVoid();
    }

    /**
     * Heartbeat sessions every 60 seconds, in order to provide a good indication in real session
     * usage
     *
     * @return
     */
    @Scheduled(every = "60s", delay = 30, delayUnit = TimeUnit.SECONDS)
    public Uni<Void> heartbeat() {
        return Uni.createFrom()
                .item(
                        this.sessionIdToSessions.values().stream()
                                .map(e -> e.getUserId())
                                .collect(Collectors.toList()))
                .chain(
                        ids -> {
                            if (!ids.isEmpty()) {
                                return this.sessionStateStore.keepUsersAlive(ids);
                            } else {
                                return Uni.createFrom().nothing();
                            }
                        });
    }

    @Scheduled(every = "180s", delay = 30, delayUnit = TimeUnit.SECONDS)
    public Uni<Void> sessionCleanup() {
        return this.lockProvider
                .tryReactiveAcquire("wsSession", Duration.ofSeconds(30))
                .chain(
                        result -> {
                            if (result) { // we are the only one doing the cleanup
                                return this.sessionStateStore.clearDeadSessions();
                            } else {
                                return Uni.createFrom().nothing();
                            }
                        });
    }

    /**
     * @return the size of the sessionStates connection remote
     */
    protected Long countAllUsers() {
        return this.sessionStateStore
                .countSessions()
                .emitOn(Infrastructure.getDefaultExecutor())
                .await()
                .indefinitely();
    }

    /**
     * @return the size of the sessionStates connection locally
     */
    protected Long countLocalUsers() {
        return Integer.valueOf(this.sessionToUser.size()).longValue();
    }

    /**
     * Called on Bean destruction, here the app stop
     *
     * <p>Will remove the local users from the remote state for Websocket Sessions and unscribre
     * from updates
     */
    @PreDestroy
    public void terminate() {
        handle
                .reactiveUnsubscribe() // Unsubscribe from all subscribed channels
                .chain(
                        () ->
                                this.sessionStateStore.removeStates(
                                        this.userIdToSession
                                                .keySet()
                                                .toArray(UUID[]::new))) // clear all local
                // connections
                .await()
                .indefinitely();
    }
}
