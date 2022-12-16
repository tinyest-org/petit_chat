package org.tyniest.chat.repository;

import java.util.List;
import java.util.UUID;

import org.tyniest.chat.entity.Message;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;

@Dao
public interface MessageRepository {
    @Insert
    void save(Message message);

    Message findById();

    List<Message> findByChatIdAndOffset(final UUID userId, final int offset);
}
