package org.tyniest.common.indexer.text.meili;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.common.indexer.text.IndexException;
import org.tyniest.common.indexer.text.SearchException;
import org.tyniest.common.indexer.text.TextIndexer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.exceptions.MeilisearchException;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MeiliIndexer implements TextIndexer {
    private final Client client;

    private int limit = 30;

    private static final String chatIdToken = "chat_id";
    private final String indexName = "messages";
    private final ObjectMapper mapper = new ObjectMapper();


    protected String getIndexName(final UUID chatId) {
        return indexName;
    }

    protected Index getIndex(final UUID chatId) throws MeilisearchException {
        return client.index(getIndexName(chatId));
    }

    @Override
    public List<UUID> fetchResult(String query, UUID chatId, int page) throws SearchException {
        try {
            final var search = new SearchRequest(query)
                .setLimit(limit)
                .setFilter(new String[] {chatIdToken + " = " + chatId}); // search in only this chat
            final var req = getIndex(chatId).search(search);
            final var res = new ArrayList<UUID>();
            req.getHits().forEach(e ->  {
                final var v = (String)e.get(chatIdToken);
                res.add(UUID.fromString(v));
            });
            return res;
        } catch (MeilisearchException e) {
            throw new SearchException();
        }
    }

    @Value(staticConstructor = "of")
    protected static class DocumentDto {
        @JsonProperty(chatIdToken)
        String chatId;
        String signalId;
        String content;
    }

    @SneakyThrows
    protected String prepareDocument(DocumentDto dto) {
        return mapper.writeValueAsString(dto);
    }

    @Override
    public void indexText(String indexName, UUID chatId, UUID signalId, String content) throws IndexException {
        try {
            final var index = getIndex(chatId);
            index.addDocuments(prepareDocument(DocumentDto.of(chatId.toString(), signalId.toString() , content)));
        } catch (MeilisearchException e) {
            log.error(e.toString());
            throw new IndexException();
        }
    }
}
