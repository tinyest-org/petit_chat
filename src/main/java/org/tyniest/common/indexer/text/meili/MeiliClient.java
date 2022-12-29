package org.tyniest.common.indexer.text.meili;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;

@ApplicationScoped
public class MeiliClient {
    @ConfigProperty(name = "meili.host")
    String host;

    @ConfigProperty(name = "meili.key")
    String key;


    @ApplicationScoped
    public Client makeClient() {
        Client client = new Client(new Config(host, key));
        return client;
    }
}
