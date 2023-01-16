package org.tyniest.chat.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationSignalDto extends BasicSignalDto {
    protected UUID chatId;
}
