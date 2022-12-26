package org.tyniest.common.indexer.text;

import java.util.List;
import java.util.UUID;

public interface TextIndexer {
    /**
     * 
     * @return the uuids of the messages
     */
    List<UUID> fetchResult(String query, int page);

    void indexText(String indexName, UUID id, String content);
}
