package org.tyniest.notification.dto;

import java.util.UUID;

import org.tyniest.utils.JsonRenderer;

import lombok.Value;

@Value(staticConstructor = "of")
public class NotificationDto {
    private String content;
    private String subject;

    public static NotificationDto ofTextSignal(String subject, UUID chatId, String content, UUID sender) {
        return NotificationDto.of(
                JsonRenderer.toJSON(
                        TextSignalDto.of(chatId, content, sender)), subject);
    }
}
