package org.tyniest.files.controller;

import java.io.FileInputStream;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.tyniest.files.dto.UploadDto;
import org.tyniest.utils.seaweed.SeaweedClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Path("/file")
public class TestFileController {
    
    private final SeaweedClient client;

    @RolesAllowed({"admin"})
    @POST
    @Path("/test")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void testFileUpload(
        UploadDto dto
    ) {
        dto.getFiles().forEach(file -> {
            final var f = file.uploadedFile().toFile();
            try (final var is = new FileInputStream(f)) {
                final var e = client.uploadFile(is).await().indefinitely();
            } catch (Exception e) {
                log.error("failed to upload: {}", e);
            }     
        });
    }
}
