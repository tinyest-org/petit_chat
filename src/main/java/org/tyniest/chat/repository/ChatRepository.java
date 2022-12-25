package org.tyniest.chat.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.entity.ChatByUser;

import com.datastax.oss.driver.api.core.PagingIterable;
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

    @Select
    PagingIterable<ChatByUser> findByUserId(final UUID userId); // TODO: as paging iterable

    @Select(customWhereClause = "id in :chatIds")
    PagingIterable<Chat> findAllByIds(final List<UUID> chatIds); // TODO: as paging iterable

}
