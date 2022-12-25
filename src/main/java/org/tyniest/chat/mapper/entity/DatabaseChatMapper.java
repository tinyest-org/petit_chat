package org.tyniest.chat.mapper.entity;

import org.tyniest.chat.repository.ChatRepository;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

@Mapper
public interface  DatabaseChatMapper {
    @DaoFactory
    ChatRepository repository();
}
