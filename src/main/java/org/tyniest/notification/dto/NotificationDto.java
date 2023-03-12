package org.tyniest.notification.dto;

import org.tyniest.chat.enums.NotificationType;
import org.tyniest.utils.JsonRenderer;

import lombok.Value;

@Value(staticConstructor = "of")
public class NotificationDto {
    private String content;
    private NotificationType notificationType;

    public static NotificationDto of(NotificationType notificationType, Object o) {
        return NotificationDto.of(JsonRenderer.toJSON(o), notificationType);
    }
}
