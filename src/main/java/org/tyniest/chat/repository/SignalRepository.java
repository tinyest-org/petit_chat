package org.tyniest.chat.repository;

import java.util.List;
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
    void save(Signal message);

    @Select
    Optional<Signal> findByChatIdAndCreatedAt(UUID chatId, UUID createdAt);
   
    @Select(customWhereClause = "chat_id = :chatId")
    PagingIterable<Signal> findByChatId(UUID chatId);

    @Select(customWhereClause = "chat_id = :chatId and created_at in :createdAtIds")
    PagingIterable<Signal> findAllByIds(UUID chatId, List<UUID> createdAtIds);
}
