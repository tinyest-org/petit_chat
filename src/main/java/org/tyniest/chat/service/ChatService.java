package org.tyniest.chat.service;

import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ForbiddenException;

import org.jboss.resteasy.reactive.multipart.FileUpload;
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
import org.tyniest.utils.seaweed.SeaweedClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    private final ObjectMapper mapper;
    private final SeaweedClient client;

    public Optional<Chat> getChat(final UUID uuid) {
        return extendedChatRepository.findById(uuid);
    }

    public void newMessage(final UUID userId, final NewMessageDto dto, final Chat chat) {
        enforceChatPermission(chat.getId(), userId);

        // TODO: upload files
        final var content = dto.getContent();
        final var createdAt = UuidHelper.timeUUID();
        final var textSignal = createTextSignal(chat.getId(), createdAt, userId, content);
        final var fileSignals = dto.getFiles().stream()
            .map(f -> createFileSignal(chat.getId(), createdAt, userId, f))
            .filter(e -> e.isPresent())
            .map(e -> e.get())
            .collect(Collectors.toList());
        // index message
        fileSignals.add(textSignal);
        saveSignalAndNotify(fileSignals);
    }

    protected Signal createTextSignal(final UUID chatId, final UUID createdAt, final UUID userId, final String content) {
        final var s = Signal.ofText(chatId, createdAt, userId, content);
        try {
            textIndexer.indexText(null, chatId, createdAt, content);
        } catch (IndexException e) {
            log.error(e.toString());
        }
        return s;
    }

    protected Optional<Signal> createFileSignal(final UUID chatId, final UUID createdAt, final UUID userId, final FileUpload content) {
        try (final var is = new FileInputStream(content.uploadedFile().toFile())) {
            final var res = client.uploadFile(is).await().indefinitely();
            final var fid = res.getFid();
            final var s = Signal.builder()
                .type(Signal.SIGNAL_FILE_TYPE)
                .chatId(chatId)
                .createdAt(createdAt)
                .userId(userId)
                .content(fid)
                .build();
            return Optional.of(s);
        } catch (Exception e) {
            log.error("failed to upload", e);
            return Optional.empty();
        }
    }

    public Chat newChat(final NewChatDto dto, final UUID userId) {
        final var id = UUID.randomUUID();
        final var c = Chat.builder()
                .id(id)
                .build();
        extendedChatRepository.save(c);
        this.addUsersInChat(id, userId, dto.getUserIds());
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
    
    @SneakyThrows
    public String pojoToJson(Object item) {
        return mapper.writeValueAsString(item);
    }

    public void saveSignalAndNotify(final List<Signal> signals) {
        signals.forEach(signal -> {
            notificationService.notifyChat(signal, signal.getChatId()); // should be users of the chat
            signalRepository.save(signal);
        });
    }
    
    public void addUsersInChat(final UUID chatId, final UUID performer ,final List<UUID> userIds) {
        enforceChatPermission(chatId, performer);
        userIds.forEach(userId -> {
            extendedChatRepository.addUserInChat(chatId, userId);
            chatRepository.save(ChatUserSettings.builder()
                .chatId(chatId)
                .userId(userId)
                .build());
        });
        final var s = pojoToJson(userIds);
        final var arrivalSignal = Signal.builder()
                .chatId(chatId)
                .content(s)
                .createdAt(UuidHelper.timeUUID())
                .build()
                .setArrivals();
        saveSignalAndNotify(List.of(arrivalSignal));
    }

    public void removeUserFromChat(final UUID chatId, final UUID performer, final List<UUID> userIds) {
        enforceChatPermission(chatId, performer);
        userIds.forEach(userId -> {
            extendedChatRepository.removeUserFromChat(chatId, userId);
            chatRepository.delete(ChatUserSettings.builder()
                .chatId(chatId)
                .userId(userId)
                .build());
        });
        final var s = pojoToJson(userIds);
        final var arrivalSignal = Signal.builder()
                .chatId(chatId)
                .content(s)
                .createdAt(UuidHelper.timeUUID())
                .build()
                .setLefts();
        saveSignalAndNotify(List.of(arrivalSignal));
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
