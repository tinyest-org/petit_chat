package org.tyniest.chat.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;

import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.entity.Signal;
import org.tyniest.chat.repository.ChatRepository;
import org.tyniest.chat.repository.SignalRepository;
import org.tyniest.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class ChatService {
    
    private final NotificationService notificationService;
    private final ChatRepository chatRepository;
    private final SignalRepository signalRepository;

    public Optional<Chat> getChat(final UUID uuid) {
        return chatRepository.findById(uuid);
    }

    public Signal newMessage(final UUID userId, final String content, final Chat chat) {
        // check chat has user in it
        if (!chat.getUserIds().contains(userId)) {
            throw new BadRequestException("not in chat");
        }
        
        final var m = Signal.builder()
            .chatId(chat.getId())
            .userId(userId)
            .content(content)
            .type("msg") // TODO: use enum
            .build(); //stub

        notificationService.notifyChat(m, chat); // should be users of the chat
        return m;
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
        return Collections.emptyList(); // stub
    }
}
