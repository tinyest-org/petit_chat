package org.tyniest.common.indexer.text.meili;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.tyniest.common.indexer.text.IndexException;
import org.tyniest.common.indexer.text.SearchException;
import org.tyniest.common.indexer.text.TextIndexer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.Settings;

import io.smallrye.mutiny.Uni;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * Compatible with meilisearch 0.27 as depdending on client compat
 */
@Slf4j
public class MeiliIndexer implements TextIndexer {
    private final Client client;

    private int limit = 30;

    private static final String signalIdToken = "signalId";
    private static final String chatIdToken = "chat_id";
    private final String indexName = "messages";
    private final ObjectMapper mapper = new ObjectMapper();


    public MeiliIndexer(final Client client) {
        this.client = client;
        initIndex();
    }

    protected String getIndexName(final UUID chatId) {
        return indexName;
    }

    @SneakyThrows
    protected void initIndex() {
        Settings settings = new Settings();
        settings.setFilterableAttributes(new String[] {chatIdToken});
        try {
            client.createIndex(indexName, signalIdToken);
        } catch (Exception e) {
            log.warn("attempted to create index");
        }
        client.index(indexName).updateSettings(settings);
    }

    protected Index getIndex(final UUID chatId) throws MeilisearchException {
        return client.index(getIndexName(chatId));
    }

    protected String[] makeFilter(final String key, final String item) {
        return new String[] {key + " = " + item};
    }

    @SneakyThrows
    protected String JSONToString(final Object o) {
        return mapper.writeValueAsString(o);
    }

    @Override
    public Uni<List<UUID>> fetchResult(String query, UUID chatId, int page) throws SearchException {
        try {
            final var search = new SearchRequest(query)
                .setLimit(limit)
                .setFilter(makeFilter(chatIdToken, chatId.toString())); // search in only this chat
            // final var c = RestClientBuilder.newBuilder()
            //     .baseUri(URI.create("https://meili.tinyest.org"))
            //     .build(CustomClient.class);
            // final var s = JSONToString(search);
            final var req = getIndex(chatId).search(search);

            final var res = req.getHits().stream()
                .map(e -> (String) e.get(signalIdToken))
                .map(e -> UUID.fromString(e))
                .collect(Collectors.toList());
            return Uni.createFrom().item(res);
        } catch (MeilisearchException  e) {
            e.printStackTrace();
            throw new SearchException();
        }
    }

    @Value(staticConstructor = "of")
    protected static class DocumentDto {
        @JsonProperty(chatIdToken)
        String chatId;
        @JsonProperty(signalIdToken)
        String signalId;
        String content;
    }

    @SneakyThrows
    protected String prepareDocument(final DocumentDto dto) {
        return mapper.writeValueAsString(dto);
    }

    @Override
    public Uni<Void> indexText(String indexName, UUID chatId, UUID signalId, String content) throws IndexException {
        try {
            final var index = getIndex(chatId);
            final var task = index.addDocuments(prepareDocument(DocumentDto.of(chatId.toString(), signalId.toString() , content)));
            return Uni.createFrom().voidItem();
        } catch (MeilisearchException e) {
            log.error(e.toString());
            throw new IndexException();
        }
    }
}
