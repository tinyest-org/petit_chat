package org.tyniest.notification.controller;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.pulsar.client.api.PulsarClientException;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.tyniest.notification.dto.NotificationDto;
import org.tyniest.notification.service.NotificationHolder;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.core.eventbus.EventBus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Path("/notifications")
public class NotificationController {
 
    private final NotificationHolder holder;
    private final EventBus bus;

    protected UUID userId = UUID.fromString("43c0db5c-d829-4929-8efc-5e4a13bb202f"); // TODO: stub

    @ConsumeEvent("register")
    public void registerCodec(final NotificationDto dto) {

    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    public Multi<NotificationDto> getNotifications() throws PulsarClientException {
        final var topic = userId.toString();
        holder.subscribeTo(topic);
        return bus.<NotificationDto>consumer(topic).bodyStream().toMulti();
    }
}
