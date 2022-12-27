package org.tyniest.config;

import javax.enterprise.context.ApplicationScoped;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.reactive.client.adapter.AdaptedReactivePulsarClientFactory;
import org.apache.pulsar.reactive.client.api.ReactivePulsarClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class QueueConfig {

    @ConfigProperty(name = "queue.pulsar.url")
    String url;

    @ApplicationScoped
    public ReactivePulsarClient makePulsar() throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(url)
                .build();
        return AdaptedReactivePulsarClientFactory.create(client);
    }
}
