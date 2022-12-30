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
import org.tyniest.common.indexer.text.IndexException;
import org.tyniest.common.indexer.text.SearchException;
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
        final var content = dto.getContent();
        final var createdAt = Uuids.timeBased();
        final var s = Signal.ofText(chat.getId(), createdAt, userId, content);
        signalRepository.save(s);
        try {
            textIndexer.indexText(null, chat.getId(), createdAt, content);
        } catch (IndexException e) {
            log.error(e.toString());
        }
        notificationService.notifyChat(s, chat); // should be users of the chat
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
        return chatRepository.findBySignalId(messagesId).all();
    }

    public List<User> getUsersInChat(final UUID chatId, final UUID userId) {
        enforceChatPermission(chatId, userId);
        return userRepository.findByChat(chatId);
    }

    public List<Signal> searchInChat(final UUID chatId, final UUID userId, final String query, final Integer page) throws SearchException {
        enforceChatPermission(chatId, userId);
        try {
            final var ids = textIndexer.fetchResult(query, chatId, page);
            return signalRepository.findAllByIds(chatId, ids).all();
        } catch (SearchException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    // TODO: handle rights
    public void addUserInChat(final UUID chatId, final UUID userId) {
        enforceChatPermission(chatId, userId);
        extendedChatRepository.addUserInChat(chatId, userId);
    }

    public void removeUserFromChat(final UUID chatId, final UUID userId) {
        enforceChatPermission(chatId, userId);
        extendedChatRepository.removeUserFromChat(chatId, userId);
    }

    public void addReaction(final UUID chatId, final UUID signalId, final UUID userId, final String value) {
        enforceChatPermission(chatId, userId);
        final var r = Reaction.builder()
                .signalId(signalId)
                .userId(userId)
                .createdAt(Uuids.timeBased())
                .value(value)
                .build();
        chatRepository.save(r);
    }

    public void removeReaction(final UUID chatId, final UUID signalId, final UUID userId, final String value) {
        enforceChatPermission(chatId, userId);
        final var r = Reaction.builder()
                .signalId(signalId)
                .userId(userId)
                .createdAt(Uuids.timeBased())
                .value(value)
                .build();
        chatRepository.delete(r);
    }

}
