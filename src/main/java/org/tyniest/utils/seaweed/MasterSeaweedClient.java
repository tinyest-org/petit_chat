package org.tyniest.utils.seaweed;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.tyniest.utils.seaweed.dto.AssignResponse;
import org.tyniest.utils.seaweed.dto.LookupResponse;

import io.smallrye.mutiny.Uni;

@RegisterRestClient
public interface MasterSeaweedClient {
    @POST
    @Path("/dir/assign")
    Uni<AssignResponse> assign();

    @GET
    @Path("/dir/lookup")
    Uni<LookupResponse> lookup(@QueryParam("volumeId") String volumeId);
}
