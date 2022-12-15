package org.tyniest.chat.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.entity.Message;
import org.tyniest.notification.service.NotificationService;
import org.tyniest.user.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class ChatService {
    
    private final NotificationService notificationService;

    public Optional<Chat> getChat(final UUID uuid) {
        return Optional.empty();
    }

    public Message newMessage(final User from, final String content, final Chat chat) {
        final var m = Message.builder().build(); //stub
        notificationService.notifyChat(m, chat); // should be users of the chat
        return m;
    }


    public List<Message> getMessagesOffsetFromEndForChat(final UUID chatId, final int offset) {
        return Collections.emptyList(); // stub
    }
}
