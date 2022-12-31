package org.tyniest.chat.controller;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.tyniest.chat.entity.ChatUserCursor;
import org.tyniest.chat.service.ChatService;
import org.tyniest.security.service.IdentityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Path("/chat/{chatId}/cursor")
public class ChatCursorController {
    
    private final IdentityService identityService;
    private final ChatService chatService;

    @PUT
    @Path("/{signalId}")
    public void updateCursor(
        @PathParam("chatId") final UUID chatId,
        @PathParam("signalId") final UUID signalId
    ) {
        chatService.updateCursor(chatId, signalId, this.identityService.getCurrentUserId());
    }
    @GET
    public List<ChatUserCursor> getCursors(
        @PathParam("chatId") final UUID chatId
    ) {
        return chatService.getCursors(chatId, this.identityService.getCurrentUserId());
    }

}
