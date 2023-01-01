package org.tyniest.chat.dto;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.hibernate.validator.constraints.Length;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewMessageDto {
    @Length(max = 4096)
    @RestForm
    @PartType(MediaType.TEXT_PLAIN)
    protected String content;

    @RestForm(FileUpload.ALL) 
    protected List<FileUpload> files;
}
