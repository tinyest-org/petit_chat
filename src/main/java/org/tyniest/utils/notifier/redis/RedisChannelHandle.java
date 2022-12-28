package org.tyniest.utils.notifier.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.tyniest.utils.notifier.ChannelHandle;

import io.quarkus.redis.datasource.pubsub.ReactivePubSubCommands;
import io.quarkus.redis.datasource.pubsub.ReactivePubSubCommands.ReactiveRedisSubscriber;
import io.smallrye.mutiny.Uni;

public class RedisChannelHandle<T> implements ChannelHandle<T> {

    private final ReactivePubSubCommands<T> pub;
    private final List<ReactiveRedisSubscriber> subscriptions = new ArrayList<>();
    private final String channel;

    public RedisChannelHandle(final String channel, final ReactivePubSubCommands<T> pub) {
        this.pub = pub;
        this.channel = channel;
    }

    @Override
    public Uni<Void> reactiveUnsubscribe() {
        return Uni.combine().all().unis(this.subscriptions.stream().map(ReactiveRedisSubscriber::unsubscribe).collect(Collectors.toList())).discardItems();
    }

    @Override
    public void unsubscribe() {
        this.reactiveUnsubscribe().await().indefinitely();
    }

    @Override
    public Uni<ChannelHandle<T>> reactiveSubscribe(Consumer<T> onMessage) {
        return pub
                .subscribe(channel, onMessage)
                .invoke(this.subscriptions::add)
                .replaceWith(this);

    }

    @Override
    public ChannelHandle<T> subscribe(Consumer<T> onMessage) {
        return this.reactiveSubscribe(onMessage).await().indefinitely();
    }

    @Override
    public Uni<ChannelHandle<T>> reactivePublish(T message) {
        return this.pub.publish(channel, message).replaceWith(this);
    }

    @Override
    public ChannelHandle<T> publish(final T message) {
        return this.reactivePublish(message).await().indefinitely();
    }

}
