package org.tyniest.chat.dto;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NewMessageDto {
    @Length(max = 4096)
    protected String content;
}
