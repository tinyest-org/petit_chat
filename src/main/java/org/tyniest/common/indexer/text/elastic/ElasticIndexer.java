package org.tyniest.common.indexer.text.elastic;

import java.util.List;
import java.util.UUID;

import org.tyniest.common.indexer.text.IndexException;
import org.tyniest.common.indexer.text.TextIndexer;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ElasticIndexer implements TextIndexer {

    private static final int PAGE_SIZE = 20;

    @Override
    public Uni<List<UUID>> fetchResult(String query, UUID chatId, int page) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uni<Void> indexText(String indexName, UUID chatId, UUID signalId, String content) throws IndexException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
