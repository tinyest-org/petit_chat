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
import org.tyniest.user.entity.User;
import org.tyniest.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Path("/chat")
@Slf4j
public class ChatController {
    // send message
    // get messages at offset

    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final SignalMapper signalMapper;
    private final UserService userService;

    protected UUID userId = UUID.fromString("43c0db5c-d829-4929-8efc-5e4a13bb202f"); // TODO: stub
    // protected UUID userId = UUID.fromString("43c0db5c-d829-4929-8efc-5e4a13bb202f"); // TODO: stub

    @POST
    @Path("/{chatId}")
    public void newMessage(
        @PathParam("chatId") final UUID chatId, 
        final NewMessageDto dto
    ) {
        final var chat = chatService.getChat(chatId).orElseThrow(NotFoundException::new);
        log.info("here");
        chatService.newMessage(userId, dto, chat);
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
        final var res = chatService.getMessagesOffsetFromEndForChat(chatId, userId, page);
        return signalMapper.asDto(res);
    }

    @GET
    @Path("/{chatId}/users")
    public List<User> getUsersInChat(
        @PathParam("chatId") final UUID chatId
    ) {
        return chatService.getUsersInChat(chatId);
    }
}
