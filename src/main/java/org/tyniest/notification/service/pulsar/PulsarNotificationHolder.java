package org.tyniest.notification.service.pulsar;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.pulsar.client.impl.schema.JSONSchema;
import org.apache.pulsar.reactive.client.adapter.AdaptedReactivePulsarClientFactory;
import org.apache.pulsar.reactive.client.api.MessageResult;
import org.apache.pulsar.reactive.client.api.MessageSpec;
import org.apache.pulsar.reactive.client.api.ReactiveMessageConsumer;
import org.apache.pulsar.reactive.client.api.ReactiveMessageSender;
import org.apache.pulsar.reactive.client.api.ReactivePulsarClient;
import org.reactivestreams.Publisher;
import org.tyniest.notification.dto.NotificationDto;
import org.tyniest.notification.service.Message;
import org.tyniest.notification.service.NotificationHolder;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.Cancellable;
import io.vertx.mutiny.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
// @ApplicationScoped
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

    protected void internalPublish(final String topic, final NotificationDto dto) {
        this.bus.publish(topic, dto);
    }

    protected ReactiveMessageConsumer<NotificationDto> makeConsumer(final String topic) {
        return this.client.messageConsumer(schema)
            .topic(topic)
            .build();
    }

    protected boolean hasTopic(final String topic) {
        return this.consumers.containsKey(topic);
    }

    protected <T> Uni<T> fromPublisher(final Publisher<T> publisher) {
        return Uni.createFrom().publisher(publisher);
    }


    @Override
    public void subscribeTo(final String topic) {
        if (hasTopic(topic)) {
            return;
        }
        final var consumer = this.makeConsumer(topic);
        final var cancellable = fromPublisher(consumer.consumeMany(flux -> flux
                        .map(message -> {
                            this.internalPublish(topic, message.getValue()); // publishing to internal bus for later use
                            return MessageResult.acknowledge(message.getMessageId());
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

    protected ReactiveMessageSender<NotificationDto> getSender(final String topic) {
        final var sender = this.client.messageSender(schema)
            .topic(topic)
            .cache(AdaptedReactivePulsarClientFactory.createCache()) // TODO: check if cache should be re used
            .build();
        return sender;
    }
    
    protected MessageSpec<NotificationDto> prepareMessage(final NotificationDto msg) {
        return  MessageSpec.of(msg);
    }

    @Override
    public Uni<Void> publish(final String topic, final List<NotificationDto> dto) {
        final var sender = getSender(topic);
        final var items = Flux.fromStream(dto.stream().map(this::prepareMessage));
        return Uni.createFrom().publisher(sender.sendMany(items)).replaceWithVoid();
    }

    @Override
    public Uni<Void> publish(final String topic, final NotificationDto dto) {
        final var sender = getSender(topic);
        sender.sendOne(prepareMessage(dto));
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Multi<Void> publish(final String topic, final Multi<NotificationDto> dto) {
        final var sender = getSender(topic);
        final var items = Flux.from(dto.map(this::prepareMessage));
        return Multi.createFrom().publisher(sender.sendMany(items)).map(e -> null);
    }

    @Override
    public Multi<Message> publish(final String topic, final Multi<NotificationDto> dto, final Duration delay) {
        final var sender = getSender(topic);
        final var items = Flux.from(dto.map(this::prepareMessage));
        sender.sendMany(items);
        // TODO Auto-generated method stub
        return null;
    }

}
