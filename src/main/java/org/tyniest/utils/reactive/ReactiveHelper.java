package org.tyniest.utils.reactive;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.reactivestreams.Publisher;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.groups.UniAndGroupIterable;
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

    public Uni<io.vertx.core.buffer.Buffer> readUpload(final FileUpload file) {
        return readFile(file.filePath());
    }

    public static <T> Uni<T> uni(Future<T> future) {
        return Uni.createFrom().future(future);
    }
    
    public static <T> Uni<T> uni(T future) {
        return Uni.createFrom().item(future);
    }
    public static <T> Uni<T> uni(CompletionStage<T> future) {
        return Uni.createFrom().completionStage(future);
    }

    public static Uni<Void> empty() {
        return Uni.createFrom().voidItem();
    }

    public static <T> Uni<T> Null() {
        return Uni.createFrom().nullItem();
    }

    public static <T> Multi<T> multi(Publisher<T> publisher) {
        return Multi.createFrom().publisher(publisher);
    }

    public static <T> Multi<T> multi(Stream<T> stream) {
        return Multi.createFrom().items(stream);
    }

    public static <T> UniAndGroupIterable<T> combineUnis(Uni<T> u1, final Uni<T> u2) {
        return Uni.combine().all().unis(List.of(u1, u2));
    }
}
