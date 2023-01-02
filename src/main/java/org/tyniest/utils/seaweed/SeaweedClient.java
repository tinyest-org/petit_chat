package org.tyniest.utils.seaweed;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.tyniest.utils.seaweed.dto.UploadBody;
import org.tyniest.utils.seaweed.dto.UploadResult;

import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SeaweedClient {
    
    private final List<String> masterUrls;
    private final List<MasterSeaweedClient> masterClients;
    private final Map<String, Set<String>> volumeIdToEndpoints = new HashMap<>();
    
    /**
     * Will hold the clients
     * <p>
     * Client are lazily built
     */
    private final Map<String, VolumeSeaweedClient> volumeClients = new HashMap<>();

    public SeaweedClient(final List<String> masterUrls) {
        this.masterUrls = masterUrls;
        this.masterClients = this.masterUrls.stream()
            .map(this::makeMasterClient)
            .collect(Collectors.toList());
    }   

    protected MasterSeaweedClient makeMasterClient(final String url) {
        return RestClientBuilder.newBuilder()
            .baseUri(prepareUrl(url))
            .build(MasterSeaweedClient.class);
    }

    protected MasterSeaweedClient getMasterClient() {
        return this.masterClients.stream().findAny().orElseThrow();
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

    protected VolumeSeaweedClient getVolumeClient(final String volumeId, final String url) {
        final var c = volumeClients.get(url);
        if (c == null) {
            final var newClient = this.buildVolumeClient(url);
            volumeClients.put(url, newClient);
            addPublicUrlToId(volumeId, Stream.of(url));
            return newClient;
        }
        return c;
    }

    protected void addPublicUrlToId(final String volumeId, final Stream<String> urls) {
        final var c = volumeIdToEndpoints.get(volumeId);
        if (c == null) {
            final var s = new HashSet<String>();
            urls.map(this::ensureProtocol).forEach(e -> s.add(e));
            volumeIdToEndpoints.put(volumeId, s);
        } else {
            urls.map(this::ensureProtocol).forEach(e -> c.add(e));
        }
    }
    
    static <E> E getRandomSetElement(Set<E> set) {
        return set.stream().skip(new Random().nextInt(set.size())).findFirst().orElseThrow();
    }

    static <E> E getRandomListElement(List<E> list) {
        final var i = new Random().nextInt(list.size());
        return list.get(i);
    }

    protected Uni<String> getVolumePublicUrl(final String volumeId) {
        final var c = volumeIdToEndpoints.get(volumeId);
        if (c != null) {
            return Uni.createFrom().item(getRandomSetElement(c));
        }
        final var masterClient = this.getMasterClient();
        return masterClient.lookup(volumeId)
            .invoke(res -> {
                this.addPublicUrlToId(volumeId, 
                    res.getLocations().stream().map(e -> e.getPublicUrl()));
            })
            .map(res -> {
                return getRandomListElement(res.getLocations()).getPublicUrl();
        });
    }
 
    /**
     * Then store file using the fid
     */
    public Uni<UploadResult> uploadFile(final InputStream file) {
        final var client = this.getMasterClient();
        return client.assign().flatMap(r -> {
            final var fid = r.getFid();
            final var volumeId = getVolumeId(fid);
            final var volumeClient = this.getVolumeClient(volumeId, r.getPublicUrl());
            return volumeClient
                .upload(fid, UploadBody.of(file))
                .map(e -> UploadResult.of(fid, e));
        });
    }

    protected String getVolumeId(final String fid) {
        final var splited = fid.split(",", 2);
        final var volumeId = splited[0];
        return volumeId;
    }

    public Uni<String> renderUrl(final String fid) {
        return getVolumePublicUrl(getVolumeId(fid)).map(e -> e + "/" + fid);
    }
}
