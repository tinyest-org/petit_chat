package org.tyniest.utils.reactive;

import javax.enterprise.context.ApplicationScoped;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.AsyncResultSet;
import com.datastax.oss.driver.api.core.cql.BatchStatementBuilder;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.BatchableStatement;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class RepositoryHelper {
    private final CqlSession session;

    public Uni<AsyncResultSet> batch(Iterable<BatchableStatement<?>> statments) {
        final var batch  = new BatchStatementBuilder(BatchType.UNLOGGED)
            .addStatements(statments)
            .build();
        return ReactiveHelper.uni(session.executeAsync(batch));
    }
}
