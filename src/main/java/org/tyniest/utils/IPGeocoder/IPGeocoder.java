package org.tyniest.utils.IPGeocoder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.tyniest.utils.IPGeocoder.IPGeocoderClient.LatLong;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class IPGeocoder {

    // taken on the website demo
    private final String fields = "66842623";
    private final String key = "ipapiq9SFY1Ic4";

    @RestClient @Inject IPGeocoderClient geocoderClient;

    public Uni<LatLong> getLatLong(final String ip) {
        return geocoderClient.getLatLong(fields, ip, key);
    }
}
