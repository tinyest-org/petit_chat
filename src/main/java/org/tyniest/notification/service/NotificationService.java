package org.tyniest.notification.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.entity.Signal;
import org.tyniest.notification.dto.NotificationDto;
import org.tyniest.user.entity.User;
import org.tyniest.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationHolder holder;
    private final UserRepository userRepository;

    public void notifyUsers(final List<User> users) {
        // TODO: implem
    }

    public void notifyUser(final User user) {
        // TODO: implem
    }

    public void notifyChat(final Signal m, final Chat chat) {
        final var userIds = userRepository.findByChatId(chat.getId());
        userIds.forEach(u -> {
            holder.publish(u.toString(), new NotificationDto(m.getContent()));
        });
    }
}
