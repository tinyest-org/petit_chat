package org.tyniest.chat.controller;

import java.util.UUID;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.tyniest.chat.dto.NewChatDto;
import org.tyniest.chat.dto.NewMessageDto;
import org.tyniest.chat.service.ChatService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Path("/chat")
public class Controller {
    // send message
    // get messages at offset

    private final ChatService chatService;

    @POST
    @Path("/{chatId}")
    public void newMessage(@PathParam("chatId") final UUID uuid, final NewMessageDto dto) {
        final var chat = chatService.getChat(uuid).orElseThrow(NotFoundException::new);
        chatService.newMessage(null, dto.getContent(), chat);
    }

    @POST
    public void newChat(final NewChatDto dto) {

    }
}
