package org.tyniest.chat.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.entity.ChatByUser;
import org.tyniest.chat.entity.Reaction;
import org.tyniest.chat.entity.UserByChat;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Query;
import com.datastax.oss.driver.api.mapper.annotations.Select;

@Dao
public interface ChatRepository {
    @Select
    Optional<Chat> findById(UUID productId);

    @Insert
    void save(Chat product);

    @Insert
    void save(UserByChat product);
    
    @Insert
    void save(ChatByUser product);

    @Insert(ifNotExists = true)
    void save(Reaction reaction);

    @Delete
    void delete(Reaction reaction);

    @Delete
    void delete(UserByChat product);
    
    @Delete
    void delete(ChatByUser product);


    @Delete
    void delete(Chat product);

    @Select
    PagingIterable<ChatByUser> findByUserId(final UUID userId);

    @Select(customWhereClause = "id in :chatIds")
    PagingIterable<Chat> findAllByIds(final List<UUID> chatIds);

    @Select(customWhereClause = "signal_id in :signalIds")
    PagingIterable<Reaction> findBySignalId(final List<UUID> signalIds);

    @Query("select count(*) from user_by_chat where chat_id = :chatId and user_id = :userId")
    long countByChatIdAndUserId(UUID chatId, UUID userId);
}
