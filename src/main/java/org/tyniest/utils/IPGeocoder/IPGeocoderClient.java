package org.tyniest.utils.IPGeocoder;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.smallrye.mutiny.Uni;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/")
@RegisterRestClient(configKey = "ip-geocoder-api")
@RegisterClientHeaders(IPGeocoderHeaderProvider.class)
public interface IPGeocoderClient {

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class LatLong {
        @JsonProperty("lat")
        private Float latitude;

        @JsonProperty("lon")
        private Float longitude;
    }

    @GET
    @Path("/json/{ip}")
    Uni<LatLong> getLatLong(
            @QueryParam("fields") String fields,
            @PathParam("ip") String ip,
            @QueryParam("key") String key);
}
