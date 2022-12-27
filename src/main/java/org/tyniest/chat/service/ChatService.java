package org.tyniest.chat.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ForbiddenException;

import org.tyniest.chat.dto.NewChatDto;
import org.tyniest.chat.dto.NewMessageDto;
import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.entity.Reaction;
import org.tyniest.chat.entity.Signal;
import org.tyniest.chat.repository.ChatRepository;
import org.tyniest.chat.repository.ExtendedSignalRepository;
import org.tyniest.chat.repository.FullChatRepository;
import org.tyniest.chat.repository.SignalRepository;
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
    private final FullChatRepository extendedChatRepository;
    private final ChatRepository chatRepository;
    private final FullUserRepository userRepository;
    private final SignalRepository signalRepository;
    private final ExtendedSignalRepository extendedSignalRepository;
    private final TextIndexer textIndexer;

    public Optional<Chat> getChat(final UUID uuid) {
        return extendedChatRepository.findById(uuid);
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
        notificationService.notifyChat(s, chat); // should be users of the chat
        // return s;
    }

    public Chat newChat(final NewChatDto dto) {
        final var id = UUID.randomUUID();
        final var c = Chat.builder()
                .id(id)
                .build();
        extendedChatRepository.save(c);
        dto.getUserIds().forEach(userId -> {
            this.addUserInChat(id, userId);
        });
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

    public List<Signal> getMessagesOffsetFromEndForChat(final UUID chatId, final UUID userId,
            final Optional<UUID> offset) {
        enforceChatPermission(chatId, userId);
        return extendedSignalRepository.findByChatId(chatId, offset).all(); // TODO handle paginantion
    }

    public List<Reaction> getReactionsForMessages(final List<UUID> messagesId) {
        // enforceChatPermission(chatId, userId);
        return chatRepository.findBySignalId(messagesId).all();
    }

    public List<User> getUsersInChat(final UUID chatId) {
        return userRepository.findByChat(chatId);
    }

    public List<Signal> getSignalsByChatAndText(final UUID chatId, final String query) {
        final var ids = textIndexer.fetchResult(query, 0);
        return signalRepository.findAllByIds(chatId, ids).all();
    }

    // TODO: handle rights
    public void addUserInChat(final UUID chatId, final UUID userId) {
        extendedChatRepository.addUserInChat(chatId, userId);
    }

    public void removeUserFromChat(final UUID chatId, final UUID userId) {
        extendedChatRepository.removeUserFromChat(chatId, userId);
    }

    public void addReaction(final UUID signalId, final UUID userId, final String value) {
        final var r = Reaction.builder()
                .signalId(signalId)
                .userId(userId)
                .createdAt(Uuids.timeBased())
                .value(value)
                .build();
        chatRepository.save(r);
    }

    public void removeReaction(final UUID signalId, final UUID userId, final String value) {
        final var r = Reaction.builder()
                .signalId(signalId)
                .userId(userId)
                .createdAt(Uuids.timeBased())
                .value(value)
                .build();
        chatRepository.delete(r);
    }

}
