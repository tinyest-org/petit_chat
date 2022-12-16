package org.tyniest.notification.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.entity.Message;
import org.tyniest.user.entity.User;

@ApplicationScoped
public class NotificationService {
    

    public void notifyUsers(final List<User> users) {
        // TODO: implem
    }

    public void notifyUser(final User user) {
        // TODO: implem
    }

    public void notifyChat(final Message m, final Chat chat) {
        final var userIds = chat.getUserIds();
    }
}
