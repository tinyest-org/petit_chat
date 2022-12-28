package org.tyniest.utils.IPGeocoder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.tyniest.utils.IPGeocoder.IPGeocoderClient.LatLong;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class IPGeocoder {

    // taken on the website demo
    @ConfigProperty(name = "geocoder.fields", defaultValue = "66842623")
    String fields;
    @ConfigProperty(name = "geocoder.key", defaultValue = "ipapiq9SFY1Ic4")
    String key;

    @RestClient @Inject IPGeocoderClient geocoderClient;

    public Uni<LatLong> getLatLong(final String ip) {
        return geocoderClient.getLatLong(fields, ip, key);
    }
}
