package org.tyniest.common.indexer.text;

import java.util.List;
import java.util.UUID;

import io.smallrye.mutiny.Uni;

public interface TextIndexer {
    /**
     * 
     * @return the uuids of the messages
     */
    Uni<List<UUID>> fetchResult(String query, UUID chatId, int page) throws SearchException;

    Uni<Void> indexText(String indexName, UUID chatId, UUID signalId, String content) throws IndexException;
}
