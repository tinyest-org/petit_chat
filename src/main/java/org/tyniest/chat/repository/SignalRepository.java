package org.tyniest.chat.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import org.tyniest.chat.entity.Signal;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;

@Dao
public interface SignalRepository {
    @Insert
    CompletionStage<Void> save(Signal message);

    @Select(customWhereClause = "chat_id = :chatId and created_at = :createdAt")
    Optional<Signal> findByChatIdAndCreatedAt(UUID chatId, UUID createdAt);
   
    @Select(customWhereClause = "chat_id = :chatId", limit = ":l")
    PagingIterable<Signal> findByChatId(UUID chatId, @CqlName("l") int l);

    @Select(customWhereClause = "chat_id = :chatId and created_at < :offset", limit = ":l")
    PagingIterable<Signal> findByChatIdAndOffset(UUID chatId, UUID offset, @CqlName("l") int l);

    @Select(customWhereClause = "chat_id = :chatId and created_at in :createdAtIds")
    PagingIterable<Signal> findAllByIds(UUID chatId, List<UUID> createdAtIds);
}
