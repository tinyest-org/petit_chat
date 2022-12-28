package org.tyniest.utils.seaweed;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.tyniest.utils.seaweed.dto.UploadBody;
import org.tyniest.utils.seaweed.dto.UploadResponse;

import io.smallrye.mutiny.Uni;

@RegisterRestClient
public interface VolumeSeaweedClient {
    @POST
    @Path("/{fid}")
    Uni<UploadResponse> upload(String fid, UploadBody body);

    @DELETE
    @Path("/{fid}")
    Uni<Void> delete(String fid);
}
