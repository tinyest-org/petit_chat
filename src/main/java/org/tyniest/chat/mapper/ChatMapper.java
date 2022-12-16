package org.tyniest.chat.mapper;

import org.tyniest.chat.repository.ChatRepository;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

@Mapper
public interface  ChatMapper {
    @DaoFactory
    ChatRepository chatRepository();
}
