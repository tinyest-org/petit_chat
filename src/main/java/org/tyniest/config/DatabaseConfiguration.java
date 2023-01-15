package org.tyniest.config;

import java.net.InetSocketAddress;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Singleton;

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

import io.quarkus.runtime.StartupEvent;


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

    void startup(@Observes StartupEvent event, CqlSession session) { 
    }

    @Singleton
    public CqlSession makeDatabaseConnection() {
        final var session = new CqlSessionBuilder()
            .addContactPoint(InetSocketAddress.createUnresolved(host, port))
            .withLocalDatacenter(datacenter)
            .withKeyspace(keyspace)
            .buildAsync().toCompletableFuture().join();
        return session;
    }

    @Singleton
    public DatabaseChatMapper makeChatMapper(final CqlSession session) {
        return new DatabaseChatMapperBuilder(session).build();
    }

    @Singleton
    public UserMapper makeUserMapper(final CqlSession session) {
        return new UserMapperBuilder(session).build();
    }

    @Singleton
    public DatabaseMessageMapper makeMessageMapper(final CqlSession session) {
        return new DatabaseMessageMapperBuilder(session).build();
    }

    @Singleton
    public ChatRepository makeChatRepo(DatabaseChatMapper chatMapper) {
        return chatMapper.repository();
    }

    @Singleton
    public SignalRepository makeMessageRepo(DatabaseMessageMapper messageMapper) {
        return messageMapper.repository();
    }

    @Singleton
    public UserRepository makeUserRepo(UserMapper userMapper) {
        return userMapper.repository();
    }

}
