package org.tyniest.user.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.repository.ChatRepository;
import org.tyniest.chat.repository.FullChatRepository;
import org.tyniest.chat.repository.SignalRepository;
import org.tyniest.user.entity.User;
import org.tyniest.user.repository.UserRepository;

import com.datastax.oss.driver.api.core.PagingIterable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class UserService {
    
    private final UserRepository userRepository;
    private final SignalRepository messageRepository;
    private final FullChatRepository chatRepository;

    public Optional<User> getUser(final UUID id) {
        return Optional.empty();
    }

    public List<Chat> getChats(final UUID userId, final int offset) {
        return chatRepository.findByUserId(userId);
    }
}
