package org.tyniest.notification.service.local;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.tyniest.notification.service.Message;
import org.tyniest.utils.reactive.ReactiveHelper;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InternalMessage implements Message {

    private final CompletableFuture<?> cancellable;


    @Override
    public Uni<Void> cancel() {
        cancellable.cancel(true);
        return ReactiveHelper.empty();
    }

    @Override
    public UUID getId() {
        // TODO Auto-generated method stub
        // not stored in database for now
        return null;
    }

    public static InternalMessage of(final CompletableFuture<?> cancellable) {
        return new InternalMessage(cancellable);
    }
    
}
