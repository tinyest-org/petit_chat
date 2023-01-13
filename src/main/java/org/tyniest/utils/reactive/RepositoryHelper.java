package org.tyniest.utils.reactive;

import javax.enterprise.context.ApplicationScoped;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.AsyncResultSet;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchStatementBuilder;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.BatchableStatement;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class RepositoryHelper {
    private final CqlSession session;

    public Uni<AsyncResultSet> unsafeBatch(Iterable<BatchableStatement<?>> statments) {
        final var batch = new BatchStatementBuilder(BatchType.UNLOGGED)
                .addStatements(statments)
                .build();
        return doBatch(batch);
    }

    public BatchAccumulator batchAccumulator() {
        return BatchAccumulator.empty();
    }

    public Uni<AsyncResultSet> safeBatch(Iterable<BatchableStatement<?>> statments) {
        final var batch = new BatchStatementBuilder(BatchType.LOGGED)
                .addStatements(statments)
                .build();
        return doBatch(batch);
    }

    public Uni<AsyncResultSet> doBatch(BatchStatement statement) {
        return ReactiveHelper.uni(session.executeAsync(statement));
    }
}
