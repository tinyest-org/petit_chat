package org.tyniest.chat.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.tyniest.user.repository.UserRepository;
import org.tyniest.utils.UniHelper;
import org.tyniest.utils.UuidHelper;
import org.tyniest.utils.reactive.ReactiveHelper;
import org.tyniest.utils.seaweed.SeaweedClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
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
    private final UserRepository baseUserRepository;
    private final FullUserRepository userRepository;
    private final SignalRepository signalRepository;
    private final ExtendedSignalRepository extendedSignalRepository;
    private final TextIndexer textIndexer;
    private final ObjectMapper mapper;
    private final SeaweedClient client;
    private final ReactiveHelper reactiveHelper;
    private final ChatContentRenderer chatContentRenderer;

    public Optional<Chat> getChat(final UUID uuid) {
        return extendedChatRepository.findById(uuid);
    }

    public Uni<List<Signal>> newMessage(final UUID userId, final NewMessageDto dto, final Chat chat) {
        return enforceChatPermission(chat.getId(), userId)
            .chain(ignored -> {

            final var content = dto.getContent();
            final var createdAt = UuidHelper.timeUUID();
            // index is done here
            final var isEmpty = content == null || content.isBlank() || content.isEmpty();

            final var fileSignals = dto.getFiles().stream()
                .map(f -> createFileSignal(chat.getId(), createdAt, userId, f))
                .map(e -> e.memoize().indefinitely())
                .collect(Collectors.toList());
            if (!isEmpty) {
                final var textSignal = createTextSignal(chat.getId(), createdAt, userId, content)
                    .memoize().indefinitely();
                fileSignals.add(textSignal);
            }
            return saveSignalAndNotify(fileSignals, chat.getId());
        });
    }

    protected Uni<Signal> createTextSignal(final UUID chatId, final UUID createdAt, final UUID userId, final String content) {
        final var s = chatContentRenderer.ofText(chatId, createdAt, userId, content);
        try {
            return textIndexer.indexText(chatId.toString(), createdAt, content).replaceWith(s);
        } catch (IndexException e) {
            log.error(e.toString());
            return Uni.createFrom().failure(e);
        }
    }

    protected Uni<Signal> createFileSignal(final UUID chatId, final UUID createdAt, final UUID userId, final FileUpload content) {
        try {
            return reactiveHelper.readUpload(content) // TODO: clean dats not pretty
                .flatMap(is -> client.uploadFile(is)
                    .map(res -> {
                        final var fid = res.getFid();
                        final var s = Signal.builder()
                            .type(Signal.FILE_TYPE)
                            .chatId(chatId)
                            .createdAt(createdAt)
                            .userId(userId)
                            .content(fid)
                            .build();
                        return s;
                }));
        } catch (Exception e) {
            log.error("failed to upload", e);
            return Uni.createFrom().failure(new Exception());
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

    public Uni<Boolean> checkChatPermission(final UUID chatId, final UUID userId) {
        return chatHasUser(chatId, userId);
    }

    public Uni<Void> enforceChatPermission(final UUID chatId, final UUID userId) {
        return checkChatPermission(chatId, userId).map(r -> {
            if (!r) {
                throw new ForbiddenException("You are not a part of this chat");
            }
            return null;
        });
    }

    public List<Signal> getMessagesOffsetFromEndForChat(final UUID chatId, final UUID userId,
            final Optional<UUID> offset) {
        final var enforced = enforceChatPermission(chatId, userId);
        enforced.await().indefinitely();
        final var req = extendedSignalRepository.findByChatId(chatId, offset); // TODO handle paginantion
        return req.all();
    }

    public List<Reaction> getReactionsForMessages(final List<UUID> messagesId) {
        return chatRepository.findBySignalId(messagesId).all();
    }

    public Uni<List<User>> getUsersInChat(final UUID chatId, final UUID userId) {
        enforceChatPermission(chatId, userId).await().indefinitely();
        return userRepository.findByChat(chatId);
    }

    public List<Signal> searchInChat(final UUID chatId, final UUID userId, final String query, final Integer page) throws SearchException {
        enforceChatPermission(chatId, userId).await().indefinitely();
        try {
            final var ids = textIndexer.fetchResult(chatId.toString(), query, page).await().indefinitely();
            return signalRepository.findAllByIds(chatId, ids).all();
        } catch (SearchException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public Uni<Boolean> chatHasUser(final UUID chatId, final UUID userId) {
        return Uni.createFrom()
            .completionStage(chatRepository.countByChatIdAndUserId(chatId, userId))
            .map(r -> r > 0);
    }
    
    @SneakyThrows
    public String pojoToJson(Object item) {
        return mapper.writeValueAsString(item);
    }

    protected <T> Uni<List<T>> applyAndCombine(final List<Uni<T>> items, final Function<T, Uni<T>> func) {
        // TODO: optimize
        final var unis = items.stream().map(i -> i.map(func).flatMap(e -> e)).collect(Collectors.toList());
        return Uni.join().all(unis).andCollectFailures();
    }

    protected <T> Uni<List<T>> batch(final List<Uni<T>> items) {
        return Uni.join().all(items).andCollectFailures();
    }

    protected <T> Uni<List<T>> batch(final Stream<Uni<T>> items) {
        return batch(items.collect(Collectors.toList()));
    }

    public Uni<List<Signal>> saveSignalAndNotify(final List<Uni<Signal>> signals, final UUID chatId) {
        if (signals.isEmpty()) {
            return Uni.createFrom().item(Collections.emptyList());
        }
        return applyAndCombine(signals, s -> {
            final var userIds = Multi.createFrom().publisher(baseUserRepository.findByChatId(chatId)).map(e -> e.getUserId()).cache();
            return notificationService.notifyChat(s, userIds)
                .flatMap(ignored -> UniHelper.uni(signalRepository.save(s)))
                .replaceWith(s);
        });
    }
   
    public Uni<Void> addUsersInChat(final UUID chatId, final UUID performer ,final List<UUID> userIds) {
        return enforceChatPermission(chatId, performer)
            .chain(ignored -> {
                final var b = batch(userIds.stream().map(userId -> {
                    return extendedChatRepository.addUserInChat(chatId, userId)
                        .flatMap(ignored2 -> {
                            return UniHelper.uni(chatRepository.save(ChatUserSettings.builder()
                                .chatId(chatId)
                                .userId(userId)
                                .build()));
                    });
                }));
                final var e = b.flatMap(u -> {
                    final var s = pojoToJson(userIds);
                    final var arrivalSignal = UniHelper.uni(
                        Signal.builder()
                            .chatId(chatId)
                            .content(s)
                            .createdAt(UuidHelper.timeUUID())
                            .build()
                            .setArrivals());
                    return saveSignalAndNotify(List.of(arrivalSignal), chatId).replaceWithVoid();
                });
                return e;
        });
    }

    public void removeUsersFromChat(final UUID chatId, final UUID performer, final List<UUID> userIds) {
        // enforceChatPermission(chatId, performer);
        // userIds.forEach(userId -> {
        //     extendedChatRepository.removeUserFromChat(chatId, userId);
        //     chatRepository.delete(ChatUserSettings.builder()
        //         .chatId(chatId)
        //         .userId(userId)
        //         .build());
        // });
        // final var s = pojoToJson(userIds);
        // final var arrivalSignal = Signal.builder()
        //         .chatId(chatId)
        //         .content(s)
        //         .createdAt(UuidHelper.timeUUID())
        //         .build()
        //         .setLefts();
        // saveSignalAndNotify(List.of(arrivalSignal));
    }

    public void addReaction(final UUID chatId, final UUID signalId, final UUID userId, final String value) {
        enforceChatPermission(chatId, userId).await().indefinitely();
        final var r = Reaction.builder()
                .signalId(signalId)
                .userId(userId)
                .createdAt(UuidHelper.timeUUID())
                .value(value)
                .build();
        chatRepository.save(r);
    }

    public void removeReaction(final UUID chatId, final UUID signalId, final UUID userId, final String value) {
        enforceChatPermission(chatId, userId).await().indefinitely();
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
        enforceChatPermission(chatId, userId).await().indefinitely();
        return chatRepository.findByChatId(chatId).all();
    }

    public Optional<ChatUserSettings> getChatUserSettings(final UUID chatId, final UUID userId) {
        return chatRepository.findByChatIdAndUserId(chatId, userId);
    }
}
