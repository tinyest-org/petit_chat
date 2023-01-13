package org.tyniest.chat.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.entity.ChatByUser;
import org.tyniest.chat.entity.ChatUserCursor;
import org.tyniest.chat.entity.ChatUserSettings;
import org.tyniest.chat.entity.Reaction;
import org.tyniest.chat.entity.UserByChat;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
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
    BoundStatement save(Chat product);

    @Insert
    BoundStatement save(ChatUserSettings settings);

    @Delete
    void delete(ChatUserSettings settings);

    @Insert
    BoundStatement save(UserByChat product);
    
    @Insert
    BoundStatement save(ChatByUser product);

    @Insert(ifNotExists = true)
    void save(ChatUserCursor cursor);

    @Delete(customWhereClause = "chat_id = :chatId and user_id = :userId", entityClass = ChatUserCursor.class)
    void delete(UUID chatId, UUID userId);
    
    @Insert(ifNotExists = true)
    void save(Reaction reaction);

    @Delete
    void delete(Reaction reaction);

    @Delete
    BoundStatement delete(UserByChat item);
    
    @Delete
    BoundStatement delete(ChatByUser item);


    @Delete
    void delete(Chat chat);

    @Select
    PagingIterable<ChatByUser> findByUserId(final UUID userId);

    @Select(customWhereClause = "id in :chatIds")
    PagingIterable<Chat> findAllByIds(final List<UUID> chatIds);

    @Select(customWhereClause = "signal_id in :signalIds")
    PagingIterable<Reaction> findBySignalId(final List<UUID> signalIds);

    @Query("select count(*) from user_by_chat where chat_id = :chatId and user_id = :userId")
    CompletionStage<Long> countByChatIdAndUserId(UUID chatId, UUID userId);

    @Select(customWhereClause = "chat_id = :chatId and user_id = :userId")
    Optional<ChatUserSettings> findByChatIdAndUserId(UUID chatId, UUID userId);

    @Select(customWhereClause = "chat_id = :chatId")
    PagingIterable<ChatUserCursor> findByChatId(UUID chatId);
}
