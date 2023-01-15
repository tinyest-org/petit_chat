package org.tyniest.utils.reactive;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchStatementBuilder;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.BatchableStatement;

public class BatchAccumulator {
    private final List<BatchableStatement<?>> statements = new ArrayList<>();

    public static BatchAccumulator empty() {
        return new BatchAccumulator();
    }

    public BatchStatement toSafeBatch() {
        final var batch = new BatchStatementBuilder(BatchType.LOGGED)
                .addStatements(statements)
                .build();
        return batch;
    }

    public List<BatchableStatement<?>> getStatements() {
        return statements;
    }

    public BatchStatement toUnsafeBatch() {

        final var batch = new BatchStatementBuilder(BatchType.UNLOGGED)
                .addStatements(statements)
                .build();
        return batch;
    }

    public BatchAccumulator add(BatchableStatement<?> statement) {
        this.statements.add(statement);
        return this;
    }

    public BatchAccumulator addAll(Iterable<BatchableStatement<?>> statements) {
        statements.forEach(statement -> {
            this.statements.add(statement);
        });
        return this;
    }

    public static BatchAccumulator ofCombined(Stream<BatchAccumulator> stream) {
        final var n = BatchAccumulator.empty();
        stream.forEach(e -> {
            n.addAll(e.getStatements());
        });
        
        return n;
    }
}
