package org.tyniest.chat.dto;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NewMessageDto {
    @Length(max = 4096)
    protected String content;

    @RestForm(FileUpload.ALL) 
    protected List<FileUpload> files;
}
