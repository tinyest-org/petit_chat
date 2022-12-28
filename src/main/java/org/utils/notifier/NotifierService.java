package org.utils.notifier;

/**
 * The notifierService is a service handling notification internal to the application
 * <p>
 * It is mostly used in order to distribute notifications accross multiple instances of the application
 */
public interface NotifierService {
    /**
     * Returns a typed handle to a given Notification Channel
     * @param <T>
     * @param channel name of the channel to get a handle to, if the channel was already acquired by another object, then throws
     * @param clazz type of the messages going trough the channel
     * @return
     * @throws ExistingConflictingChannel
     */
    <T> ChannelHandle<T> getHandle(String channel, Class<T> clazz) throws ExistingConflictingChannel;
}
