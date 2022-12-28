package org.tyniest.utils.notifier.redis;

import java.util.HashSet;
import java.util.Set;

import org.tyniest.utils.notifier.ChannelHandle;
import org.tyniest.utils.notifier.ExistingConflictingChannel;
import org.tyniest.utils.notifier.NotifierService;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;

public class RedisNotifier implements NotifierService {

    private final ReactiveRedisDataSource ds;
    private final Set<String> handles = new HashSet<>();

    public RedisNotifier(final ReactiveRedisDataSource ds) {
        this.ds = ds;
    }

    @Override
    public <T> ChannelHandle<T> getHandle(final String channel, final Class<T> clazz)
            throws ExistingConflictingChannel {
        final var current = this.handles.contains(channel);
        if (current) {
            throw new ExistingConflictingChannel();
        }
        this.handles.add(channel);
        final var pub = this.ds.pubsub(clazz);
        return new RedisChannelHandle<>(channel, pub);
    }
}
