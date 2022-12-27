package org.tyniest.config;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.mapper.entity.DatabaseChatMapper;
import org.tyniest.chat.mapper.entity.DatabaseMessageMapper;
import org.tyniest.chat.repository.ChatRepository;
import org.tyniest.chat.repository.SignalRepository;
import org.tyniest.user.mapper.UserMapper;
import org.tyniest.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class RepoConfig {
    
    private final DatabaseChatMapper chatMapper;
    private final DatabaseMessageMapper messageMapper;
    private final UserMapper userMapper;
    
    @ApplicationScoped
    public ChatRepository makeChatRepo() {
        return chatMapper.repository();
    }

    @ApplicationScoped
    public SignalRepository makeMessageRepo() {
        return messageMapper.repository();
    }

    @ApplicationScoped
    public UserRepository makeUserRepo() {
        return userMapper.repository();
    }
}
