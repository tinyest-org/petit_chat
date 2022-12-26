package org.tyniest.chat.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignalDto {
    protected UUID uuid;
    protected String content;
    protected String type;
    protected Instant createdAt;
}
