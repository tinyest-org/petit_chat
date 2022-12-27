package org.tyniest.config;

import javax.enterprise.context.ApplicationScoped;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class QueueConfig {

    @ConfigProperty(name = "queue.pulsar.url")
    String url;

    @ApplicationScoped
    public PulsarClient makePulsar() throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(url)
                .build();
        return client;
    }
}
