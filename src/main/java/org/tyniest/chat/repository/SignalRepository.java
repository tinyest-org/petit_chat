package org.tyniest.chat.repository;

import java.util.List;
import java.util.UUID;

import org.tyniest.chat.entity.Signal;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;

@Dao
public interface SignalRepository {
    @Insert
    void save(Signal message);

    @Select
    Signal findById(UUID id);

    @Select
    List<Signal> findByChatIdAndOffset(final UUID userId, final int offset);
}
