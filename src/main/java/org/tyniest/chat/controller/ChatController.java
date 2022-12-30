package org.tyniest.chat.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.tyniest.chat.dto.BasicSignalDto;
import org.tyniest.chat.dto.NewChatDto;
import org.tyniest.chat.dto.NewMessageDto;
import org.tyniest.chat.dto.SignalDto;
import org.tyniest.chat.entity.Reaction;
import org.tyniest.chat.mapper.ChatMapper;
import org.tyniest.chat.mapper.SignalMapper;
import org.tyniest.chat.service.ChatService;
import org.tyniest.common.indexer.text.SearchException;
import org.tyniest.security.service.IdentityService;
import org.tyniest.user.entity.User;
import org.tyniest.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Path("/chat")
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final SignalMapper signalMapper;
    private final UserService userService;
    private final IdentityService identityService;

    @POST
    @Path("/{chatId}")
    public void newMessage(
        @PathParam("chatId") final UUID chatId, 
        final NewMessageDto dto
    ) {
        final var chat = chatService.getChat(chatId).orElseThrow(NotFoundException::new);
        final var userId = identityService.getCurrentUserId();
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
        @QueryParam("lastMessage") Optional<UUID> lastMessage
    ) {
        final var userId = identityService.getCurrentUserId();
        final var res = chatService.getMessagesOffsetFromEndForChat(chatId, userId, lastMessage);
        final var reactions = chatService.getReactionsForMessages(res.stream().map(e -> e.getCreatedAt()).collect(Collectors.toList()));
        final var e = new HashMap<UUID, List<Reaction>>();
        res.forEach(a ->  {
            e.put(a.getCreatedAt(), new ArrayList<>());
        });
        reactions.forEach(r -> {
            e.get(r.getSignalId()).add(r);
        });
        return res.stream()
            .map(a -> signalMapper.asDto(a, e.get(a.getCreatedAt())))
            .collect(Collectors.toList());
    }

    @GET
    @Path("/{chatId}/search")
    public List<BasicSignalDto> searchInChat(
        @PathParam("chatId") final UUID chatId, 
        @NotBlank @QueryParam("q") String query,
        @DefaultValue("0") @QueryParam("page") Integer page
        
    ) throws SearchException {
        final var userId = identityService.getCurrentUserId();
        final var reactions = chatService.searchInChat(chatId, userId, query, page);
        return signalMapper.asBasicDto(reactions.stream());
    }

    @GET
    @Path("/{chatId}/users")
    public List<User> getUsersInChat(
        @PathParam("chatId") final UUID chatId
    ) {
        return chatService.getUsersInChat(chatId, this.identityService.getCurrentUserId());
    }

    @PUT
    @Path("/{chatId}/user/{userId}")
    public void addUserInChat(
        @PathParam("chatId") final UUID chatId,
        @PathParam("userId") final UUID userId
    ) {
        chatService.addUserInChat(chatId, userId);
    }

    @DELETE
    @Path("/{chatId}/user/{userId}")
    public void deleteUserFromChat(
        @PathParam("chatId") final UUID chatId,
        @PathParam("userId") final UUID userId
    ) {
        chatService.addUserInChat(chatId, userId);
    }
}
