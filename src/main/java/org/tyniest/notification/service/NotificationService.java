package org.tyniest.notification.service;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.entity.Signal;
import org.tyniest.notification.dto.NotificationDto;
import org.tyniest.user.entity.User;
import org.tyniest.user.repository.UserRepository;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
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

    // public void notifyChat(final Signal m, final Chat chat) {
    //     notifyChat(m, chat.getId());
    // }

    public Uni<Void> notifyUsers(final String subject, final Signal m, final Multi<UUID> userIds) {
        return userIds
                .invoke(u -> {
                    holder.publish(u.toString(), List.of(new NotificationDto(m.getContent(), subject)));
                })
                .collect()
                .asList()
                .replaceWithVoid();
    }
}
