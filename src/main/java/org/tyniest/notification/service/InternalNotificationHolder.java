package org.tyniest.notification.service;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.notification.dto.NotificationDto;

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
    public Uni<Void> publish(String topic, NotificationDto dto) {
        this.bus.publish(topic, dto);
        log.info("sent: {}, to topic: {}", dto, topic);
        return Uni.createFrom().voidItem();
    }
}
