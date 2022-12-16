package org.tyniest.config;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.mapper.ChatMapper;
import org.tyniest.chat.repository.ChatRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class RepoConfig {
    
    private final ChatMapper chatMapper;
    
    @ApplicationScoped
    public ChatRepository makeChatRepo() {
        return chatMapper.chatRepository();
    }
}
