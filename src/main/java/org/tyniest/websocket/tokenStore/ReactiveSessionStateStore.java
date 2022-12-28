package org.tyniest.websocket.tokenStore;

import java.util.List;

import org.tyniest.websocket.state.SessionState;

import io.smallrye.mutiny.Uni;


/** Basic interface to a Websocket token store */
public interface ReactiveSessionStateStore {

    /** Tells wether this store is local or not */
    boolean isLocal();

    /**
     * Returns the name of the SessionStateStore The name should give an indication on the type of
     * the implementation
     */
    String getName();

    /**
     * @param token
     * @return the state associated to a given token, it provides the feature to associate a user to
     *     a session using a unique token
     */
    Uni<SessionState> getStateByToken(final String token);

    /** Deletes the token associated to a given userId */
    Uni<Void> deleteTokenIfExistsForId(final Long userId);

    /** Puts a token for a given user and sets the state accordingly */
    Uni<Void> putToken(final String token, final Long userId, final SessionState state);

    /** Removes everything associated to a given token for a userId */
    Uni<Void> deleteToken(final String token, final Long userId);

    /** Removes all the sessionStates from the Store */
    Uni<Void> clearAll();

    /** put the state for a given userId */
    Uni<Void> putState(final Long userId, final SessionState state);

    /**
     * Removes the state corresponding to a given userId
     *
     * @param userId
     * @return
     */
    Uni<Boolean> removeState(final Long userId);

    /**
     * Like removeState but handling many Ids Should be used instead of {@link #removeState(Long)}
     * as some implementation can handle batching
     */
    Uni<Boolean> removeStates(final Long[] userIds);

    /** Returns all the current connected users */
    Uni<List<SessionState>> getAllStates();

    /**
     * Use it in order to prevent {@link #clearDeadSessions()} from garbage collecting it
     *
     * <p>It's a hearbeat, in order to tell wich sessions are still alive
     */
    Uni<Void> keepUsersAlive(final List<Long> userIds);

    /**
     * Clears dead sessions wich did not get correctly disconnected,
     *
     * <p>ie: Instance crash, instance network disconnection
     *
     * <p>It's a clean function which should be used periodiccally
     *
     * <p>In the case of a non local store, make sure to acquire a lock to the instance or method
     */
    Uni<Void> clearDeadSessions();

    /**
     * @return the number of connected users to this SessionStateStore
     */
    Uni<Long> countSessions();
}
