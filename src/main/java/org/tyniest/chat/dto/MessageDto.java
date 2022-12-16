package org.tyniest.chat.dto;

import java.util.UUID;

import io.quarkus.arc.All;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    protected UUID uuid;
    protected String content;
}
