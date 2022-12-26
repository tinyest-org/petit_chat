package org.tyniest.chat.controller;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.hibernate.validator.constraints.NotBlank;
import org.tyniest.chat.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Path("/chat/{chatId}/{signalId}")
public class ReactionController {
    
    private final ChatService chatService;
    protected UUID userId = UUID.fromString("43c0db5c-d829-4929-8efc-5e4a13bb202f"); // TODO: stub

    @PUT
    @Path("/{value}")
    public void addReaction(
        @PathParam("chatId") final UUID chatId,
        @PathParam("signalId") final UUID signalId,
        @NotEmpty @PathParam("value") final String value
    ) {
        chatService.addReaction(signalId, userId, value);
    }

    @DELETE
    @Path("/{value}")
    public void removeReaction(
        @PathParam("chatId") final UUID chatId,
        @PathParam("signalId") final UUID signalId,
        @NotEmpty @PathParam("value") final String value
    ) {
        chatService.removeReaction(signalId, userId, value);
    }
}
