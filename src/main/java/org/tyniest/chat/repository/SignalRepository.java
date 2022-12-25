package org.tyniest.chat.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.tyniest.chat.entity.Signal;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;

@Dao
public interface SignalRepository {
    @Insert
    Signal save(Signal message);

    @Select
    Optional<Signal> findByChatIdAndCreatedAt(UUID chatId, Instant createdAt);

    @Select(customWhereClause = "chat_id = :chatId")
    PagingIterable<Signal> findByChatId(UUID chatId);
    // @Select
    // List<Signal> findByChatIdAndOffset(final UUID userId, final int offset);
}
