package org.tyniest.common.indexer.text.meili;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.tyniest.common.indexer.text.IndexException;
import org.tyniest.common.indexer.text.SearchException;
import org.tyniest.common.indexer.text.TextIndexer;
import org.tyniest.utils.UniHelper;

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

    private final Set<String> initedIndexess = new HashSet<>();
    private final ManagedExecutor managedExecutor;

    public MeiliIndexer(final Client client, ManagedExecutor managedExecutor) {
        this.client = client;
        this.managedExecutor = managedExecutor;
    }

    protected String getIndexName(final String name) {
        return name;
    }

    @SneakyThrows
    protected Uni<Void> ensureIndex(final String name) {
        // TODO: should handle locks here
        if (this.initedIndexess.contains(name)) {
            return UniHelper.Void();
        }
        final var fut = managedExecutor.submit(() -> {
            final var settings = new Settings();
            settings.setFilterableAttributes(new String[] {chatIdToken});
            try {
                client.createIndex(name, signalIdToken);
            } catch (Exception e) {
                log.warn("attempted to create index");
            }
            try {
                client.index(name).updateSettings(settings);
            } catch (Exception e) {
                log.warn("Failed to create index");
            }
            this.initedIndexess.add(name);
        });
        return UniHelper.uni(fut).replaceWithVoid();
    }

    protected Index getIndex(final String name) throws MeilisearchException {
        return client.index(getIndexName(name));
    }

    protected String[] makeFilter(final String key, final String item) {
        return new String[] {key + " = " + item};
    }

    @SneakyThrows
    protected String JSONToString(final Object o) {
        return mapper.writeValueAsString(o);
    }

    @Override
    public Uni<List<UUID>> fetchResult(String indexName, String query, int page) throws SearchException {
        this.ensureIndex(indexName);
        try {
            final var search = new SearchRequest(query)
                .setLimit(limit);
            // final var c = RestClientBuilder.newBuilder()
            //     .baseUri(URI.create("https://meili.tinyest.org"))
            //     .build(CustomClient.class);
            // final var s = JSONToString(search);
            final var req = getIndex(indexName).search(search);

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
        @JsonProperty(signalIdToken)
        String signalId;
        String content;
    }

    @SneakyThrows
    protected String prepareDocument(final DocumentDto dto) {
        return mapper.writeValueAsString(dto);
    }

    @Override
    public Uni<Void> indexText(String indexName, UUID signalId, String content) throws IndexException {
        try {
            final var index = getIndex(indexName);
            final var task = index.addDocuments(prepareDocument(DocumentDto.of(signalId.toString() , content)));
            return Uni.createFrom().voidItem();
        } catch (MeilisearchException e) {
            log.error(e.toString());
            throw new IndexException();
        }
    }
}
