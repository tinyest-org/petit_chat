package org.tyniest.chat.dto.notification;

import java.util.UUID;

import org.tyniest.chat.dto.BasicSignalDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationSignalDto extends BasicSignalDto {
    protected UUID chatId;
}
