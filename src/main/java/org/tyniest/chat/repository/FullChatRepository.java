package org.tyniest.chat.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.entity.ChatByUser;
import org.tyniest.chat.entity.UserByChat;
import org.tyniest.utils.reactive.BatchAccumulator;
import org.tyniest.utils.reactive.RepositoryHelper;

import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BoundStatement;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class FullChatRepository {

    private final ChatRepository chatRepository;
    private final RepositoryHelper repoHelpler;

    public Optional<Chat> findById(UUID productId) {
        return chatRepository.findById(productId);
    }

    public BoundStatement save(Chat product) {
        return chatRepository.save(product);
    }

    public void delete(Chat product) {
        chatRepository.delete(product);
    }

    public List<Chat> findByUserId(final UUID userId) {
        final var res = chatRepository.findByUserId(userId).map(e -> e.getChatId()).all();
        return chatRepository.findAllByIds(res).all();
    }

    public BatchAccumulator addUserInChat(UUID chatId, UUID userId) {
        final var s1 = chatRepository.save(UserByChat.builder()
            .chatId(chatId)
            .userId(userId)
            .build());
        final var s2 = chatRepository.save(ChatByUser.builder()
            .chatId(chatId)
            .userId(userId)
            .build());
        return repoHelpler.batchAccumulator()
            .add(s1)
            .add(s2);
    }

    public Uni<Void> removeUserFromChat(final UUID chatId, final UUID userId) {
        final var s1 = chatRepository.delete(UserByChat.builder()
            .chatId(chatId)
            .userId(userId)
            .build());
        final var s2 = chatRepository.delete(ChatByUser.builder()
            .chatId(chatId)
            .userId(userId)
            .build());
        return repoHelpler.unsafeBatch(List.of(s1, s2)).replaceWithVoid();
    }
}
