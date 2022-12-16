package org.tyniest.chat.service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BadRequestException;

import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.entity.Message;
import org.tyniest.chat.repository.ChatRepository;
import org.tyniest.chat.repository.MessageRepository;
import org.tyniest.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class ChatService {
    
    private final NotificationService notificationService;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    public Optional<Chat> getChat(final UUID uuid) {
        return chatRepository.findById(uuid);
    }

    public Message newMessage(final UUID userId, final String content, final Chat chat) {
        // check chat has user in it
        if (!chat.getUserIds().contains(userId)) {
            throw new BadRequestException("not in chat");
        }
        
        final var m = Message.builder()
            .chatId(chat.getId())
            .userId(userId)
            .content(content)
            .build(); //stub

        notificationService.notifyChat(m, chat); // should be users of the chat
        return m;
    }

    public void setMessageDeleted(final UUID messageId, final UUID chatId, final UUID userId) {
        // TODO: implem
        // should attempt to write at messageId if has correct chatId and userId
    }


    public List<Message> getMessagesOffsetFromEndForChat(final UUID chatId, final int offset) {
        return Collections.emptyList(); // stub
    }
}
