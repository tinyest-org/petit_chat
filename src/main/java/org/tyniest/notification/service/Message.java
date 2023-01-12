package org.tyniest.notification.service;

import java.util.UUID;

import io.smallrye.mutiny.Uni;

public interface Message {
    Uni<Void> cancel();
    UUID getId();
}
