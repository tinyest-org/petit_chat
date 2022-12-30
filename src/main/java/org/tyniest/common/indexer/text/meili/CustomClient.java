package org.tyniest.common.indexer.text.meili;


import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;

@RegisterClientHeaders(CustomClientHeaderProvider.class)
public interface CustomClient {

    @POST
    @Path("/indexes/{indexName}/search")
    String search(@PathParam("indexName") String indexName, CustomSearchRequest body);
}
