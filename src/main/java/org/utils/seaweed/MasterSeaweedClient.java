package org.utils.seaweed;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.utils.seaweed.dto.AssignResponse;

import io.smallrye.mutiny.Uni;

@RegisterRestClient
public interface MasterSeaweedClient {
    @POST
    @Path("/dir/assign")
    Uni<AssignResponse> assign();
}
