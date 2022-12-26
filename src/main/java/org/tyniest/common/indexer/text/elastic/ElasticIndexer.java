package org.tyniest.common.indexer.text.elastic;

import java.util.List;
import java.util.UUID;

import org.tyniest.common.indexer.text.TextIndexer;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ElasticIndexer implements TextIndexer {

    private static final int PAGE_SIZE = 20;

    @Override
    public List<UUID> fetchResult(String query, int page) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void indexText(String indexName, UUID id, String content) {
        // TODO Auto-generated method stub
        
    }
    
}
