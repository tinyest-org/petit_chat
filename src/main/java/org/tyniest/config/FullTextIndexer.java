package org.tyniest.config;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.common.indexer.text.TextIndexer;
import org.tyniest.common.indexer.text.elastic.ElasticIndexer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class FullTextIndexer {
    @ApplicationScoped
    public TextIndexer makeTextIndexer() {
        return new ElasticIndexer();
    }
}
