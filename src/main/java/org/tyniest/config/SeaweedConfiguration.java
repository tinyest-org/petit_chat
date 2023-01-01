package org.tyniest.config;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.utils.seaweed.SeaweedClient;

@ApplicationScoped
public class SeaweedConfiguration {
    
    private final List<String> urls = List.of("https://seaweed.tinyest.org");

    @ApplicationScoped
    public SeaweedClient makeClient() {
        return new SeaweedClient(urls);
    }
}
