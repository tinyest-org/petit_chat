package org.tyniest.user.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.entity.Chat;
import org.tyniest.user.entity.User;

@ApplicationScoped
public class UserService {
    

    public Optional<User> getUser(final UUID id) {
        return Optional.empty();
    }

    public List<Chat> getChats(final User user, final int offset) {
        return Collections.emptyList();
    }
}
