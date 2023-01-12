package org.tyniest.notification.service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.notification.dto.NotificationDto;

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
        return;
    }

    @Override
    public void unsubsribeFrom(String topic) {
        return;
    }

    @Override
    public Uni<Void> publish(String topic, List<NotificationDto> dto) {
        this.bus.publish(topic, dto);
        log.info("sent: {}, to topic: {}", dto, topic);
        return Uni.createFrom().voidItem();
    }

    @Override
    public Uni<Void> publish(String topic, NotificationDto dto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Multi<Void> publish(String topic, Multi<NotificationDto> dto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Multi<UUID> publish(String topic, Multi<NotificationDto> dto, Duration delay) {
        // TODO Auto-generated method stub
        return null;
    }
}
