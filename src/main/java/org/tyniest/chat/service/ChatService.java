package org.tyniest.chat.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ForbiddenException;

import org.tyniest.chat.dto.NewChatDto;
import org.tyniest.chat.dto.NewMessageDto;
import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.entity.ChatUserCursor;
import org.tyniest.chat.entity.ChatUserSettings;
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
import org.tyniest.utils.UuidHelper;

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
        final var createdAt = UuidHelper.timeUUID();
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
        return chatHasUser(chatId, userId);
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

    public boolean chatHasUser(final UUID chatId, final UUID userId) {
        return chatRepository.countByChatIdAndUserId(chatId, userId) > 0;
    }

    public void addUserInChat(final UUID chatId, final UUID userId) {
        enforceChatPermission(chatId, userId);
        extendedChatRepository.addUserInChat(chatId, userId);
        chatRepository.save(ChatUserSettings.builder()
            .chatId(chatId)
            .userId(userId)
            .build());
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
                .createdAt(UuidHelper.timeUUID())
                .value(value)
                .build();
        chatRepository.save(r);
    }

    public void removeReaction(final UUID chatId, final UUID signalId, final UUID userId, final String value) {
        enforceChatPermission(chatId, userId);
        final var r = Reaction.builder()
                .signalId(signalId)
                .userId(userId)
                .createdAt(UuidHelper.timeUUID())
                .value(value)
                .build();
        chatRepository.delete(r);
    }

    public void updateCursor(final UUID chatId, final UUID signalId, final UUID userId) {
        enforceChatPermission(chatId, userId);
        // TODO: check we are going to an existing signal which is after the current one
        chatRepository.delete(chatId, userId);
        chatRepository.save(ChatUserCursor.builder()
            .userId(userId)
            .chatId(chatId)
            .lastSignalRead(signalId)
            .updatedAt(UuidHelper.timeUUID())
            .build());
    }

    public List<ChatUserCursor> getCursors(final UUID chatId, final UUID userId) {
        enforceChatPermission(chatId, userId);
        return chatRepository.findByChatId(chatId).all();
    }

    public Optional<ChatUserSettings> getChatUserSettings(final UUID chatId, final UUID userId) {
        return chatRepository.findByChatIdAndUserId(chatId, userId);
    }
}
