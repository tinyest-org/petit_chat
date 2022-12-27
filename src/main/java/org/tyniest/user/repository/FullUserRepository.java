package org.tyniest.user.repository;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.repository.ChatRepository;
import org.tyniest.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class FullUserRepository {
    private final UserRepository userRepository;
    
    public List<User> findByChat(final UUID chatId) {
        final var res = userRepository.findByChatId(chatId).map(e -> e.getUserId()).all();
        return userRepository.findAllByIds(res).all();
    }
}
