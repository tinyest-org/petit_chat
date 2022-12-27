package org.tyniest.config;

import java.net.InetSocketAddress;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.tyniest.chat.mapper.entity.DatabaseChatMapper;
import org.tyniest.chat.mapper.entity.DatabaseChatMapperBuilder;
import org.tyniest.chat.mapper.entity.DatabaseMessageMapper;
import org.tyniest.chat.mapper.entity.DatabaseMessageMapperBuilder;
import org.tyniest.chat.repository.ChatRepository;
import org.tyniest.chat.repository.SignalRepository;
import org.tyniest.user.mapper.UserMapper;
import org.tyniest.user.mapper.UserMapperBuilder;
import org.tyniest.user.repository.UserRepository;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;


@ApplicationScoped
public class DatabaseConfiguration {
    
    @ConfigProperty(name = "db.host")
    String host;

    @ConfigProperty(name = "db.port")
    Integer port;

    @ConfigProperty(name = "db.datacenter")
    String datacenter;
    
    @ConfigProperty(name = "db.keyspace")
    String keyspace;

    @ApplicationScoped
    public CqlSession makeDatabaseConnection() {
        return new CqlSessionBuilder()
            .addContactPoint(InetSocketAddress.createUnresolved(host, port))
            .withLocalDatacenter(datacenter)
            .withKeyspace(keyspace)
            .build();
    }

    @ApplicationScoped
    public DatabaseChatMapper makeChatMapper(final CqlSession session) {
        return new DatabaseChatMapperBuilder(session).build();
    }

    @ApplicationScoped
    public UserMapper makeUserMapper(final CqlSession session) {
        return new UserMapperBuilder(session).build();
    }

    @ApplicationScoped
    public DatabaseMessageMapper makeMessageMapper(final CqlSession session) {
        return new DatabaseMessageMapperBuilder(session).build();
    }

    @ApplicationScoped
    public ChatRepository makeChatRepo(DatabaseChatMapper chatMapper) {
        return chatMapper.repository();
    }

    @ApplicationScoped
    public SignalRepository makeMessageRepo(DatabaseMessageMapper messageMapper) {
        return messageMapper.repository();
    }

    @ApplicationScoped
    public UserRepository makeUserRepo(UserMapper userMapper) {
        return userMapper.repository();
    }

}
