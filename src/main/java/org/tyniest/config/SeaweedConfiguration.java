package org.tyniest.config;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.utils.seaweed.SeaweedClient;

@ApplicationScoped
public class SeaweedConfiguration {
    @ApplicationScoped
    public SeaweedClient makeClient() {
        return new SeaweedClient("https://seaweed.tinyest.org");
    }
}
