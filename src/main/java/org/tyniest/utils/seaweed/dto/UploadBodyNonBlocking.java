package org.tyniest.utils.seaweed.dto;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.PartType;

import io.vertx.core.buffer.Buffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadBodyNonBlocking {
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    private Buffer file;

    public static UploadBodyNonBlocking of(Buffer file) {
        return new UploadBodyNonBlocking(file);
    }
}
