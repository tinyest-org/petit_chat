package org.utils.seaweed;

import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.utils.seaweed.dto.UploadBody;
import org.utils.seaweed.dto.UploadResponse;

import io.smallrye.mutiny.Uni;

public class SeaweedClient {
    
    private final List<String> masterUrls;
    private final List<MasterSeaweedClient> masterClients;
    /**
     * Will hold the clients
     * <p>
     * Client are lazily built
     */
    private final Map<String, VolumeSeaweedClient> volumeClients = new HashMap<>();

    public SeaweedClient(final String masterUrl) {
        this.masterUrls = Arrays.asList(masterUrl);
        this.masterClients = this.masterUrls.stream().map(this::makeMasterClient).collect(Collectors.toList());
    }   

    protected MasterSeaweedClient makeMasterClient(final String url) {
        return RestClientBuilder.newBuilder()
            .baseUri(URI.create(url))
            .build(MasterSeaweedClient.class);
    }

    protected MasterSeaweedClient getMasterClient() {
        return this.masterClients.get(0);
    }

    protected VolumeSeaweedClient buildVolumeClient(final String url) {
        return RestClientBuilder.newBuilder()
            .baseUri(URI.create(url))
            .build(VolumeSeaweedClient.class);
    }

    protected VolumeSeaweedClient getVolumeClient(final String url) {
        final var c = volumeClients.get(url);
        if (c == null) {
            final var newClient = this.buildVolumeClient(url);
            volumeClients.put(url, newClient);
        }
        return c;
    }
 
    public Uni<UploadResponse> uploadFile(final InputStream file) {
        final var client = this.getMasterClient();
        return client.assign().flatMap(r -> {
            final var volumeClient = this.getVolumeClient(r.getUrl());
            return volumeClient.upload(r.getFid(), UploadBody.of(file));
        });
    }
}
