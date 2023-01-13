package org.tyniest.notification.service.local;

import java.time.Duration;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.notification.dto.NotificationDto;
import org.tyniest.notification.service.Message;
import org.tyniest.notification.service.NotificationHolder;
import org.tyniest.utils.reactive.ReactiveHelper;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;

// @Priority(2)
@ApplicationScoped
@Slf4j
public class InternalNotificationHolder implements NotificationHolder {

    private final EventBus bus;

    public InternalNotificationHolder(final EventBus bus) {
        this.bus = bus;
    }

    @Override
    public void subscribeTo(String topic) {
        // local so always subscribed
    }

    @Override
    public void unsubsribeFrom(String topic) {
        // local so always subscribed
    }

    @Override
    public Uni<Void> publish(String topic, List<NotificationDto> dto) {
        this.bus.publish(topic, dto);
        log.debug("sent: {}, to topic: {}", dto, topic);
        return ReactiveHelper.empty();
    }

    @Override
    public Uni<Void> publish(String topic, NotificationDto dto) {
        return publish(topic, List.of(dto));
    }

    @Override
    public Multi<Void> publish(String topic, Multi<NotificationDto> dto) {
        return publish(topic, dto, Duration.ZERO).map(e -> null);
    }

    @Override
    public Multi<Message> publish(String topic, Multi<NotificationDto> dto, Duration delay) {
        return dto.map(
                e -> Uni.createFrom().nothing()
                        .onItem()
                        .delayIt().by(delay)
                        .invoke(() -> bus.publish(topic, e)))
                .map(e -> e.subscribe().asCompletionStage())
                .map(e -> InternalMessage.of(e));
    }

}
