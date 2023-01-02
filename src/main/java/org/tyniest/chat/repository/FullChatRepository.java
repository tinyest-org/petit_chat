package org.tyniest.chat.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.entity.ChatByUser;
import org.tyniest.chat.entity.UserByChat;
import org.tyniest.utils.UniHelper;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class FullChatRepository {
    private final ChatRepository chatRepository;

    
    public Optional<Chat> findById(UUID productId) {
        return chatRepository.findById(productId);
    }

    public void save(Chat product) {
        chatRepository.save(product);
    }

    public void delete(Chat product) {
        chatRepository.delete(product);
    }

    public List<Chat> findByUserId(final UUID userId) {
        final var res = chatRepository.findByUserId(userId).map(e -> e.getChatId()).all();
        return chatRepository.findAllByIds(res).all();
    }

    public Uni<Void> addUserInChat(UUID chatId, UUID userId) {
        final var u1 = UniHelper.uni(chatRepository.save(UserByChat.builder()
            .chatId(chatId)
            .userId(userId)
            .build()));
        final var u2 = UniHelper.uni(chatRepository.save(ChatByUser.builder()
            .chatId(chatId)
            .userId(userId)
            .build()));
        return Uni.combine().all().unis(u1, u2).discardItems();
    }

    public void removeUserFromChat(UUID chatId, UUID userId) {
        chatRepository.delete(UserByChat.builder()
            .chatId(chatId)
            .userId(userId)
            .build());
        chatRepository.delete(ChatByUser.builder()
            .chatId(chatId)
            .userId(userId)
            .build());
    }
}
