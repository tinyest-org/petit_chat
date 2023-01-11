package org.tyniest.config;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.tyniest.common.indexer.text.TextIndexer;
import org.tyniest.common.indexer.text.meili.MeiliIndexer;

import com.meilisearch.sdk.Client;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class FullTextIndexer {
    // TODO: define order with multiple text indexers

    private final ManagedExecutor managedExecutor;

    @ApplicationScoped
    public TextIndexer makeMeiliTextIndexer(Client client) {
        return new MeiliIndexer(client, managedExecutor);
    }
}
