package org.tyniest.utils.reactive;

import java.nio.file.Path;

import javax.enterprise.context.ApplicationScoped;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class ReactiveHelper {
    private final Vertx vertx;

    public Uni<io.vertx.core.buffer.Buffer> readFile(final Path path) {
        return vertx.fileSystem().readFile(path.toString()).map(Buffer::getDelegate);
    }

    public Uni<Buffer> readMutinyFile(final Path path) {
        return vertx.fileSystem().readFile(path.toString());
    }
}
