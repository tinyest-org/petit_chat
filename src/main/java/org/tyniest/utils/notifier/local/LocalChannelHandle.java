package org.tyniest.utils.notifier.local;

import java.util.function.Consumer;

import org.tyniest.utils.notifier.ChannelHandle;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.MessageConsumer;
import io.vertx.mutiny.core.eventbus.MessageProducer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalChannelHandle<T> implements ChannelHandle<T> {

    private final EventBus bus;
    private final String topic;
    private final MessageConsumer<T> consumer;
    private final MessageProducer<T> producer;
    private final Class<T> type;


    public LocalChannelHandle(final String topic, final EventBus bus, final Class<T> clazz) {
        this.topic = topic;
        this.bus = bus;
        this.type = clazz;
        this.handleCodec();
        this.consumer = this.bus.consumer(this.topic);
        this.producer = this.bus.publisher(this.topic);
    }

    protected void handleCodec() {
        final var codec = new IdentityCodec<>(this.type);
        // this.bus.unregisterCodec(codec.name());
        try {
            this.bus.registerCodec(codec);
        } catch (Exception e) {

        }
        
        log.info("added codec");
    }

    @Override
    public ChannelHandle<T> subscribe(Consumer<T> onMessage) {
        this.reactiveSubscribe(onMessage).await().indefinitely();
        return this;
    }

    @Override
    public Uni<ChannelHandle<T>> reactiveSubscribe(Consumer<T> onMessage) {
        this.consumer.bodyStream().handler(onMessage);
        return Uni.createFrom().item(this);
    }

    @Override
    public void unsubscribe() {
        this.reactiveUnsubscribe().await().indefinitely();
        
    }

    @Override
    public Uni<Void> reactiveUnsubscribe() {
        return this.consumer.unregister();
    }

    @Override
    public ChannelHandle<T> publish(T message) {
        this.reactivePublish(message).await().indefinitely();
        return this;
    }

    @Override
    public Uni<ChannelHandle<T>> reactivePublish(T message) {
        return this.producer.write(message).replaceWith(this);
    }
    
}
