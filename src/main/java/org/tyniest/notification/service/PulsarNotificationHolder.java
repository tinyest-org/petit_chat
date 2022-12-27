package org.tyniest.notification.service;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import org.apache.pulsar.client.impl.schema.JSONSchema;
import org.apache.pulsar.reactive.client.adapter.AdaptedReactivePulsarClientFactory;
import org.apache.pulsar.reactive.client.api.MessageResult;
import org.apache.pulsar.reactive.client.api.MessageSpec;
import org.apache.pulsar.reactive.client.api.ReactiveMessageConsumer;
import org.apache.pulsar.reactive.client.api.ReactivePulsarClient;
import org.tyniest.notification.dto.NotificationDto;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.Cancellable;
import io.vertx.mutiny.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class PulsarNotificationHolder implements NotificationHolder {
    private final ReactivePulsarClient client;
    private final EventBus bus;
    private final Map<String, Cancellable> consumers = new HashMap<>();
    private final JSONSchema<NotificationDto> schema = JSONSchema.of(NotificationDto.class);
    public PulsarNotificationHolder(
            final ReactivePulsarClient client,
            final EventBus bus) {
        this.client = client;
        this.bus = bus;
    }

    protected void internalPublish(String topic, NotificationDto dto) {
        this.bus.publish(topic, dto);
    }

    protected ReactiveMessageConsumer<NotificationDto> makeConsumer(String topic) {
        return this.client.messageConsumer(schema)
            .topic(topic)
            .build();
    }

    protected boolean hasTopic(String topic) {
        return this.consumers.containsKey(topic);
    }

    

    @Override
    public void subscribeTo(final String topic) {
        if (hasTopic(topic)) {
            return;
        }
        final var consumer = this.makeConsumer(topic);
        final var cancellable = Uni.createFrom()
                .publisher(consumer.consumeMany(flux -> flux
                        .map(message -> {
                            this.internalPublish(topic, message.getValue()); // publishing to internal bus for later use
                            return MessageResult.acknowledge(message.getMessageId(), message.getValue());
                        })))
                .subscribe()
                .with(suc -> {
                    log.info("successfully subsribed to: {}", topic);
                }, err -> {
                    log.error("Failed to subsribe to: {}", topic);
                });
        this.consumers.put(topic, cancellable);
    }

    @Override
    public void unsubsribeFrom(final String topic) {
        final var current = this.consumers.get(topic);
        if (topic == null) {
            return;
        }
        current.cancel();
    }
    
    @Override
    public Uni<Void> publish(final String topic, final NotificationDto dto) {
        final var sender = this.client.messageSender(schema)
            .topic(topic)
            .cache(AdaptedReactivePulsarClientFactory.createCache())
            .build();
        return Uni.createFrom().publisher(sender.sendOne(MessageSpec.of(dto))).replaceWithVoid();
    }

}
