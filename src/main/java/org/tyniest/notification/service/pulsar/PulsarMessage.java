package org.tyniest.notification.service.pulsar;

import java.util.UUID;

import org.tyniest.notification.service.Message;

import io.smallrye.mutiny.Uni;

public class PulsarMessage implements Message {

    @Override
    public Uni<Void> cancel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UUID getId() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
