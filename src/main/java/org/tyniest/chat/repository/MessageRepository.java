package org.tyniest.chat.repository;

import org.tyniest.chat.entity.Message;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;

@Dao
public interface MessageRepository {
    @Insert
    void save(Message message);
}
