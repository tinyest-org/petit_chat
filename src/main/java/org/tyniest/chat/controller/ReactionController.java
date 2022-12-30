package org.tyniest.chat.controller;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.tyniest.chat.service.ChatService;
import org.tyniest.chat.service.ReactionService;
import org.tyniest.security.service.IdentityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Path("/chat/{chatId}/{signalId}")
public class ReactionController {
    
    private final ReactionService reactionService;
    private final ChatService chatService;
    private final IdentityService identityService;

    @PUT
    @Path("/{value}")
    public void addReaction(
        @PathParam("chatId") final UUID chatId,
        @PathParam("signalId") final UUID signalId,
        @NotEmpty @PathParam("value") final String value
    ) {
        if (reactionService.checkReactionType(value)) {
            chatService.addReaction(chatId, signalId, identityService.getCurrentUserId(), value);
        } else {

        }
        
    }

    @DELETE
    @Path("/{value}")
    public void removeReaction(
        @PathParam("chatId") final UUID chatId,
        @PathParam("signalId") final UUID signalId,
        @NotEmpty @PathParam("value") final String value
    ) {
        if (reactionService.checkReactionType(value)) {
            chatService.removeReaction(chatId, signalId, identityService.getCurrentUserId(), value);
        } else {

        }
    }
}
