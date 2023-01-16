package org.tyniest.notification.dto;

import org.tyniest.utils.JsonRenderer;

import lombok.Value;

@Value(staticConstructor = "of")
public class NotificationDto {
    private String content;
    private String subject;

    public static NotificationDto of(String subject, Object o) {
        return NotificationDto.of(JsonRenderer.toJSON(o), subject);
    }
}
