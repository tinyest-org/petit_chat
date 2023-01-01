package org.tyniest.utils.seaweed;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.tyniest.utils.seaweed.dto.UploadBody;
import org.tyniest.utils.seaweed.dto.UploadResponse;

import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SeaweedClient {
    
    private final List<String> masterUrls;
    private final List<MasterSeaweedClient> masterClients;
    /**
     * Will hold the clients
     * <p>
     * Client are lazily built
     */
    private final Map<String, VolumeSeaweedClient> volumeClients = new HashMap<>();

    public SeaweedClient(final List<String> masterUrls) {
        this.masterUrls = masterUrls;
        this.masterClients = this.masterUrls.stream().map(this::makeMasterClient).collect(Collectors.toList());
    }   

    protected MasterSeaweedClient makeMasterClient(final String url) {
        return RestClientBuilder.newBuilder()
            .baseUri(prepareUrl(url))
            .build(MasterSeaweedClient.class);
    }

    protected MasterSeaweedClient getMasterClient() {
        return this.masterClients.get(0);
    }

    protected String ensureProtocol(final String url) {
        if (url.startsWith("http://")
            || url.startsWith("https://")
        ) {
            return url;
        }
        return "https://" + url;
    }

    protected URI prepareUrl(final String url) {
        return URI.create(ensureProtocol(url));
    }

    protected VolumeSeaweedClient buildVolumeClient(final String url) {
        return RestClientBuilder.newBuilder()
            .baseUri(prepareUrl(url))
            .build(VolumeSeaweedClient.class);
    }

    protected VolumeSeaweedClient getVolumeClient(final String url) {
        final var c = volumeClients.get(url);
        if (c == null) {
            final var newClient = this.buildVolumeClient(url);
            volumeClients.put(url, newClient);
            return newClient;
        }
        return c;
    }
 
    public Uni<UploadResponse> uploadFile(final InputStream file) {
        final var client = this.getMasterClient();
        return client.assign().flatMap(r -> {
            final var volumeClient = this.getVolumeClient(r.getPublicUrl());
            log.info("{} -> {}", r.getFid());
            return volumeClient.upload(r.getFid(), UploadBody.of(file));
        });
    }
}
