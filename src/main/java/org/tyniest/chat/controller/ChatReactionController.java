package org.tyniest.chat.controller;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.tyniest.chat.service.ChatService;
import org.tyniest.chat.service.ReactionService;
import org.tyniest.security.service.IdentityService;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Path("/chat/{chatId}/{signalId}/reaction")
public class ChatReactionController {
    
    private final ReactionService reactionService;
    private final ChatService chatService;
    private final IdentityService identityService;

    @PUT
    @Path("/{value}")
    public Uni<Void> addReaction(
        @PathParam("chatId") final UUID chatId,
        @PathParam("signalId") final UUID signalId,
        @NotEmpty @PathParam("value") final String value
    ) {
        if (reactionService.checkReactionType(value)) {
            return chatService.addReaction(chatId, signalId, identityService.getCurrentUserId(), value);
        } else {
            throw new BadRequestException("invalid reaction");
        }
        
    }

    @DELETE
    @Path("/{value}")
    public Uni<Void> removeReaction(
        @PathParam("chatId") final UUID chatId,
        @PathParam("signalId") final UUID signalId,
        @NotEmpty @PathParam("value") final String value
    ) {
        // no need to check here, if value does not exists then nothing is done
        return chatService.removeReaction(chatId, signalId, identityService.getCurrentUserId(), value);
    }
}
