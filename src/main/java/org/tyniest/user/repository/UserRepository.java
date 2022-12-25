package org.tyniest.user.repository;

import java.util.List;
import java.util.UUID;

import org.tyniest.chat.entity.UserByChat;
import org.tyniest.user.entity.User;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Select;

@Dao
public interface UserRepository {
    @Select
    PagingIterable<UserByChat> findByChatId(final UUID chatId);
    
    @Select(customWhereClause = "id in :userIds")
    PagingIterable<User> findAllByIds(final List<UUID> userIds); // TODO: as paging iterable
}
