package org.tyniest.chat.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;

import org.tyniest.chat.dto.NewChatDto;
import org.tyniest.chat.dto.NewMessageDto;
import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.entity.Signal;
import org.tyniest.chat.repository.ChatRepository;
import org.tyniest.chat.repository.FullChatRepository;
import org.tyniest.chat.repository.SignalRepository;
import org.tyniest.notification.service.NotificationService;
import org.tyniest.user.entity.User;
import org.tyniest.user.repository.FullUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class ChatService {
    
    private final NotificationService notificationService;
    private final FullChatRepository chatRepository;
    private final FullUserRepository userRepository;
    private final SignalRepository signalRepository;

    public Optional<Chat> getChat(final UUID uuid) {
        return chatRepository.findById(uuid);
    }

    public Signal newMessage(final UUID userId, final NewMessageDto dto, final Chat chat) {
        enforceChatPermission(chat.getId(), userId); 
        
        // TODO: upload files
        final var s = Signal.builder()
            .chatId(chat.getId())
            .userId(userId)
            .content(dto.getContent())
            .type("text") // TODO: use enum
            .build(); //stub
        signalRepository.save(s);
        notificationService.notifyChat(s, chat); // should be users of the chat
        return s;
    }

    public Chat newChat(final NewChatDto dto) {
        final var c = Chat.builder()
            // .userIds(Arrays.asList(UUID.randomUUID()))
            .build();
        chatRepository.save(c);
        return c;
    }

    public void setMessageDeleted(final UUID messageId, final UUID chatId, final UUID userId) {
        // TODO: implem
        // should attempt to write at messageId if has correct chatId and userId
    }

    public boolean checkChatPermission(final UUID chatId, final UUID userId) {
        return true; // TODO: dats a stub
    }

    public void enforceChatPermission(final UUID chatId, final UUID userId) {
        if (!checkChatPermission(chatId, userId)) {
            throw new ForbiddenException("You are not a part of this chat");
        }
    }

    public void toggleSignalReaction(final UUID chatId, final UUID userId, final UUID messageId) {
        enforceChatPermission(chatId, userId);
        // TODO: implem
    }

    public List<Signal> getMessagesOffsetFromEndForChat(final UUID chatId, final UUID userId ,final Long offset) {
        enforceChatPermission(chatId, userId);
        return signalRepository.findByChatId(chatId).all(); // TODO handle paginantion
    }


    public List<User> getUsersInChat(final UUID chatId) {
        return userRepository.findByChat(chatId);
    }
}
