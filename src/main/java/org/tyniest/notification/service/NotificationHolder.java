package org.tyniest.notification.service;

import org.tyniest.notification.dto.NotificationDto;

import io.smallrye.mutiny.Uni;

public interface NotificationHolder {
    void subscribeTo(final String topic);
    void unsubsribeFrom(final String topic);
    Uni<Void> publish(final String topic, final NotificationDto dto);
}
