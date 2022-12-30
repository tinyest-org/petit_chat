package org.tyniest.common.indexer.text.meili;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

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
    public List<UUID> fetchResult(String query, UUID chatId, int page) throws SearchException {
        try {
            final var search = new SearchRequest(query)
                .setLimit(limit)
                .setFilter(makeFilter(chatIdToken, chatId.toString())); // search in only this chat
            // final var c = RestClientBuilder.newBuilder()
            //     .baseUri(URI.create("https://meili.tinyest.org"))
            //     .build(CustomClient.class);
            final var s = JSONToString(search);
            // log.info("result: {}", c.search(indexName, search));
            final var req = getIndex(chatId).search(search);
            log.info("hits: {}", req.getNbHits());
            final var res = new ArrayList<UUID>();
            req.getHits().forEach(e ->  {
                final var v = (String) e.get(signalIdToken);
                final var k = UUID.fromString(v);
                res.add(k);
            });
            return res;
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
