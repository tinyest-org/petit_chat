package org.tyniest.chat.controller;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.tyniest.chat.dto.NewChatDto;
import org.tyniest.chat.dto.NewMessageDto;
import org.tyniest.chat.dto.SignalDto;
import org.tyniest.chat.mapper.ChatMapper;
import org.tyniest.chat.mapper.SignalMapper;
import org.tyniest.chat.service.ChatService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Path("/chat")
public class ChatController {
    // send message
    // get messages at offset

    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final SignalMapper signalMapper;

    @POST
    @Path("/{chatId}")
    public void newMessage(
        @PathParam("chatId") final UUID uuid, 
        final NewMessageDto dto
    ) {
        final var chat = chatService.getChat(uuid).orElseThrow(NotFoundException::new);
        chatService.newMessage(null, dto, chat);
    }

    @POST
    public void newChat(final NewChatDto dto) {
        chatService.newChat(dto);
    }


    @GET
    @Path("/{chatId}")
    public List<SignalDto> getMessagesOffsetForChat(
        @PathParam("chatId") final UUID chatId, 
        @QueryParam("page") Long page
    ) {
        UUID userId = UUID.fromString("43c0db5c-d829-4929-8efc-5e4a13bb202f"); // TODO: stub
        final var res = chatService.getMessagesOffsetFromEndForChat(chatId, userId, page);
        return signalMapper.asDto(res);
    }
}
