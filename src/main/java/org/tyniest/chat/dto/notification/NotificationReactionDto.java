package org.tyniest.chat.dto.notification;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationReactionDto {
    protected boolean add;
    protected UUID userId;
    protected String value;
    protected UUID signalId;
}
