package org.tyniest.config;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.mapper.entity.DatabaseChatMapper;
import org.tyniest.chat.mapper.entity.DatabaseChatMapperBuilder;
import org.tyniest.chat.mapper.entity.DatabaseMessageMapper;
import org.tyniest.chat.mapper.entity.DatabaseMessageMapperBuilder;
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
    public DatabaseChatMapper  makeChatMapper() {
        return new DatabaseChatMapperBuilder(session).build();
    }

    @ApplicationScoped
    public UserMapper  makeUserMapper() {
        return new UserMapperBuilder(session).build();
    }

    @ApplicationScoped
    public DatabaseMessageMapper  makeMessageMapper() {
        return new DatabaseMessageMapperBuilder(session).build();
    }
}
