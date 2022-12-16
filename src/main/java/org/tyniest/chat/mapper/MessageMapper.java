package org.tyniest.chat.mapper;

import org.tyniest.chat.repository.MessageRepository;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

@Mapper
public interface  MessageMapper {
    @DaoFactory
    MessageRepository repository();
}
