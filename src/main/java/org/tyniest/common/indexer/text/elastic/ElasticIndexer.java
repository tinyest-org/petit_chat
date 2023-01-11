package org.tyniest.common.indexer.text.elastic;

import java.util.List;
import java.util.UUID;

import org.tyniest.common.indexer.text.IndexException;
import org.tyniest.common.indexer.text.SearchException;
import org.tyniest.common.indexer.text.TextIndexer;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ElasticIndexer implements TextIndexer {@Override
    public Uni<List<UUID>> fetchResult(String indexName, String query, int page) throws SearchException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uni<Void> indexText(String indexName, UUID signalId, String content) throws IndexException {
        // TODO Auto-generated method stub
        return null;
    }
  
}
