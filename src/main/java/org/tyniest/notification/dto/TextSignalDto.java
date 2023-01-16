package org.tyniest.notification.dto;

import java.util.UUID;

import lombok.Value;

@Value(staticConstructor = "of")
public class TextSignalDto {
    protected UUID chatId;
    protected String content;
    protected UUID sender;
}
