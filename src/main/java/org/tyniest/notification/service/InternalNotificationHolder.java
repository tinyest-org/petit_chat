package org.tyniest.notification.service;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.notification.dto.NotificationDto;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;

@ApplicationScoped
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
        return Uni.createFrom().voidItem();
    }
}
