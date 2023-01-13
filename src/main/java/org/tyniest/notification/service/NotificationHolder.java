package org.tyniest.notification.service;

import java.time.Duration;
import java.util.List;

import org.tyniest.notification.dto.NotificationDto;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface NotificationHolder {
    void subscribeTo(final String topic);
    void unsubsribeFrom(final String topic);
    
    /**
     * List 
     * @param topic
     * @param dto
     * @param delay
     * @return the ids of the newly created notifications, can be used to cancel the notifications
     */
    Uni<Void> publish(final String topic, final NotificationDto dto);
    
    /**
     * List 
     * @param topic
     * @param dto
     * @param delay
     * @return the ids of the newly created notifications, can be used to cancel the notifications
     */
    Uni<Void> publish(final String topic, final List<NotificationDto> dto);
        
    /**
     * List 
     * @param topic
     * @param dto
     * @param delay
     * @return the ids of the newly created notifications, can be used to cancel the notifications
     */
    Multi<Void> publish(final String topic, final Multi<NotificationDto> dto);
    /**
     * List 
     * @param topic
     * @param dto
     * @param delay
     * @return the ids of the newly created notifications, can be used to cancel the notifications
     */
    Multi<Message> publish(final String topic, final Multi<NotificationDto> dto, final Duration delay);
}
