package org.tyniest.chat.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BasicSignalDto {
    protected UUID uuid;
    protected String content;
    protected Integer type;
    protected Instant createdAt;
    protected UUID userId;
}
