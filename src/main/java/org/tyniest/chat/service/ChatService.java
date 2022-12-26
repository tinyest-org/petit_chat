package org.tyniest.chat.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ForbiddenException;

import org.tyniest.chat.dto.NewChatDto;
import org.tyniest.chat.dto.NewMessageDto;
import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.entity.Signal;
import org.tyniest.chat.repository.FullChatRepository;
import org.tyniest.chat.repository.FullSignalRepository;
import org.tyniest.common.indexer.text.TextIndexer;
import org.tyniest.notification.service.NotificationService;
import org.tyniest.user.entity.User;
import org.tyniest.user.repository.FullUserRepository;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class ChatService {
    
    private final NotificationService notificationService;
    private final FullChatRepository chatRepository;
    private final FullUserRepository userRepository;
    private final FullSignalRepository signalRepository;
    private final TextIndexer textIndexer;

    public Optional<Chat> getChat(final UUID uuid) {
        return chatRepository.findById(uuid);
    }

    public void newMessage(final UUID userId, final NewMessageDto dto, final Chat chat) {
        enforceChatPermission(chat.getId(), userId); 
        
        // TODO: upload files
        final var s = Signal.builder()
            .chatId(chat.getId())
            .userId(userId)
            .createdAt(Uuids.timeBased())
            .content(dto.getContent())
            .type("text") // TODO: use enum
            .build();
        signalRepository.save(s);
        notificationService.notifyChat(null, chat); // should be users of the chat
        // return s;
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

    public List<Signal> getSignalsByChatAndText(final UUID chatId, final String query) {
        final var ids = textIndexer.fetchResult(query, 0);
        return signalRepository.findAllByIds(chatId, ids).all();
    }

}
