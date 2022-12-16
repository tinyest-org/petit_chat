package org.tyniest.chat.repository;

import java.util.Optional;
import java.util.UUID;

import org.tyniest.chat.entity.Chat;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;

@Dao
public interface ChatRepository {
    @Select
    Optional<Chat> findById(UUID productId);

    @Insert
    void save(Chat product);
  
    @Delete
    void delete(Chat product);
}
