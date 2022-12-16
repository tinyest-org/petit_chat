package org.tyniest.config;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.mapper.ChatMapper;
import org.tyniest.chat.mapper.ChatMapperBuilder;

import com.datastax.oss.driver.api.core.CqlSession;

import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DatabaseMapperConfig {
        
    private final CqlSession session;
    
    @ApplicationScoped
    public ChatMapper  makeChatRepo() {
        return new ChatMapperBuilder(session).build();
    }
}
