package org.tyniest.config;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.mapper.ChatMapper;
import org.tyniest.chat.mapper.MessageMapper;
import org.tyniest.chat.repository.ChatRepository;
import org.tyniest.chat.repository.MessageRepository;
import org.tyniest.user.mapper.UserMapper;
import org.tyniest.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;


// TODO: create all repos
@RequiredArgsConstructor
@ApplicationScoped
public class RepoConfig {
    
    private final ChatMapper chatMapper;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;
    
    @ApplicationScoped
    public ChatRepository makeChatRepo() {
        return chatMapper.repository();
    }

    @ApplicationScoped
    public MessageRepository makeMessageRepo() {
        return messageMapper.repository();
    }

    @ApplicationScoped
    public UserRepository makeUserRepo() {
        return userMapper.repository();
    }
}
