package org.tyniest.config;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.mapper.entity.ChatMapper;
import org.tyniest.chat.mapper.entity.ChatMapperBuilder;
import org.tyniest.chat.mapper.entity.MessageMapper;
import org.tyniest.chat.mapper.entity.MessageMapperBuilder;
import org.tyniest.user.mapper.UserMapper;
import org.tyniest.user.mapper.UserMapperBuilder;

import com.datastax.oss.driver.api.core.CqlSession;

import lombok.RequiredArgsConstructor;


// TODO: create all mappers
@ApplicationScoped
@RequiredArgsConstructor
public class DatabaseMapperConfig {
        
    private final CqlSession session;
    
    @ApplicationScoped
    public ChatMapper  makeChatMapper() {
        return new ChatMapperBuilder(session).build();
    }

    @ApplicationScoped
    public UserMapper  makeUserMapper() {
        return new UserMapperBuilder(session).build();
    }

    @ApplicationScoped
    public MessageMapper  makeMessageMapper() {
        return new MessageMapperBuilder(session).build();
    }
}
