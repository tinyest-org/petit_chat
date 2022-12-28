package org.tyniest.websocket.tokenStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.tyniest.websocket.state.SessionState;

import io.smallrye.mutiny.Uni;


/** Token store implemented on top of the Base Java Hashmap */
// @Alternative
// @ApplicationScoped
public class LocalTokenStore implements ReactiveSessionStateStore {
    // userId -> token
    private final Map<UUID, String> valueIdCommands = new HashMap<>();
    // token
    // token -> state
    private final Map<String, SessionState> valueCommands = new HashMap<>();

    private final Map<UUID, SessionState> sessionStates = new HashMap<>();

    public Uni<SessionState> getStateByToken(final String token) {
        return Uni.createFrom().item(this.valueCommands.get(token));
    }

    @Override
    public Uni<Void> clearAll() {
        return Uni.createFrom()
                .voidItem()
                .invoke(
                        () -> {
                            this.valueCommands.clear();
                            this.valueIdCommands.clear();
                            this.sessionStates.clear();
                        });
    }

    public Uni<Void> deleteTokenIfExistsForId(final UUID userId) {
        return Uni.createFrom()
                .item(this.valueIdCommands.get(userId))
                .flatMap(
                        token -> {
                            if (token != null) {
                                return this.deleteToken(token, userId);
                            }
                            return Uni.createFrom().voidItem();
                        });
    }

    public Uni<Void> putToken(final String token, final UUID userId, final SessionState state) {
        return Uni.createFrom()
                .voidItem()
                .invoke(
                        () -> {
                            valueCommands.put(token, state);
                            this.valueIdCommands.put(userId, token);
                        });
    }

    public Uni<Void> deleteToken(final String token, final UUID userId) {
        return Uni.createFrom()
                .voidItem()
                .invoke(
                        () -> {
                            this.valueCommands.remove(token);
                            this.valueIdCommands.remove(userId);
                        });
    }

    @Override
    public Uni<Void> putState(UUID id, SessionState state) {
        return Uni.createFrom().voidItem().invoke(() -> this.sessionStates.put(id, state));
    }

    @Override
    public Uni<Boolean> removeState(UUID id) {
        return Uni.createFrom()
                .item(
                        () -> {
                            this.sessionStates.remove(id);
                            return true;
                        });
    }

    @Override
    public Uni<List<SessionState>> getAllStates() {
        return Uni.createFrom()
                .item(
                        this.sessionStates.entrySet().stream()
                                .map(e -> e.getValue())
                                .collect(Collectors.toList()));
    }

    @Override
    public Uni<Boolean> removeStates(UUID[] ids) {
        return Uni.createFrom()
                .item(
                        () -> {
                            for (final var i : ids) {
                                this.sessionStates.remove(i);
                            }
                            return true;
                        });
    }

    @Override
    public String getName() {
        return "local";
    }

    /** This store is not distributed, so no need to have a real implementation */
    @Override
    public Uni<Void> keepUsersAlive(List<UUID> userIds) {
        return Uni.createFrom().nothing();
    }

    /** This store is not distributed, so no need to have a real implementation */
    @Override
    public Uni<Void> clearDeadSessions() {
        return Uni.createFrom().nothing();
    }

    @Override
    public boolean isLocal() {
        return true;
    }

    @Override
    public Uni<Long> countSessions() {
        return Uni.createFrom().item(this.sessionStates::size).map(Integer::longValue);
    }
}
