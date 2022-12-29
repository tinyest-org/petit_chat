package org.tyniest.common.indexer.text;

import java.util.List;
import java.util.UUID;

public interface TextIndexer {
    /**
     * 
     * @return the uuids of the messages
     */
    List<UUID> fetchResult(String query, UUID chatId, int page) throws SearchException;

    void indexText(String indexName, UUID chatId, UUID signalId, String content) throws IndexException;
}
