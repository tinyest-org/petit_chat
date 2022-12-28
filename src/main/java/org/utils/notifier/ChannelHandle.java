package org.utils.notifier;

import java.util.function.Consumer;

import io.smallrye.mutiny.Uni;

/**
 * A handle to a given channel
 * <p>
 * A handle is typed with <T> and bound to a name
 * <p>
 * The Notifier service, providing the Handles, ensures that no 2 handle with
 * the same name and a differerent type exist at the same time
 */
public interface ChannelHandle<T> {
    /**
     * Adds a consumer to the Channel handle
     *
     * @param onMessage
     * @return
     */
    ChannelHandle<T> subscribe(Consumer<T> onMessage);

    /**
     * Adds a consumer to the Channel handle
     *
     * @param onMessage
     * @return
     */
    Uni<ChannelHandle<T>> reactiveSubscribe(Consumer<T> onMessage);

    /**
     * Closes the channel
     */
    void unsubscribe();

    /**
     * Closes the channel
     */
    Uni<Void> reactiveUnsubscribe();

    /**
     * Publishes the message of type T to the channel
     *
     * @param message
     * @return
     */
    ChannelHandle<T> publish(T message);

    /**
     * Publishes the message of type T to the channel
     *
     * @param message
     * @return
     */
    Uni<ChannelHandle<T>> reactivePublish(T message);
}
