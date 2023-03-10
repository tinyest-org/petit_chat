package org.tyniest.user.repository;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.user.entity.User;
import org.tyniest.utils.reactive.ReactiveHelper;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class FullUserRepository {
    private final UserRepository userRepository;

    public Uni<List<User>> findByChat(final UUID chatId) {
        final var m = ReactiveHelper.multi(userRepository.findByChatId(chatId)).map(e -> e.getUserId()).collect().asList();
        return m.flatMap(res -> {
            final var m2 = ReactiveHelper.multi(userRepository.findAllByIds(res)).collect().asList();
            return m2;
        });
    }
}
