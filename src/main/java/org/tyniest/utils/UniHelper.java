package org.tyniest.utils;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

import javax.enterprise.event.Observes;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;

public class UniHelper {
    public static <T> Uni<T> uni(Future<T> future) {
        return Uni.createFrom().future(future);
    }

    public static <T> Uni<T> uni(T future) {
        return Uni.createFrom().item(future);
    }
    public static <T> Uni<T> uni(CompletionStage<T> future) {
        return Uni.createFrom().completionStage(future);
    }

    public static Uni<Void> Void() {
        return Uni.createFrom().voidItem();
    }

    public static <T> Uni<T> Null() {
        return Uni.createFrom().nullItem();
    }
}
