package org.tyniest.notification.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public class NotificationDto {
    private String content;
    private String subject;
}
