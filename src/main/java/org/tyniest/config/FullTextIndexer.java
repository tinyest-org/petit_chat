package org.tyniest.config;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.common.indexer.text.TextIndexer;
import org.tyniest.common.indexer.text.meili.MeiliIndexer;

import com.meilisearch.sdk.Client;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class FullTextIndexer {
    // TODO: define order with multiple text indexers
    @ApplicationScoped
    public TextIndexer makeMeiliTextIndexer(Client client) {
        return new MeiliIndexer(client);
    }
}
