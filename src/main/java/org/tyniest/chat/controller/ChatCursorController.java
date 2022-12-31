package org.tyniest.chat.controller;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.tyniest.chat.service.ChatService;
import org.tyniest.security.service.IdentityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Path("/chat/{chatId}/cursor")
public class ChatCursorController {
    
    private final IdentityService identityService;
    private final ChatService chatService;

    @GET
    public void get(
        @PathParam("chatId") final UUID chatId
    ) {
        final var userId = this.identityService.getCurrentUserId();
    }

    @PATCH
    public void update(
        @PathParam("chatId") final UUID chatId
    ) {
        final var userId = this.identityService.getCurrentUserId();
    }
}
