package org.tyniest.user.repository;

import java.util.List;
import java.util.UUID;

import org.tyniest.chat.entity.UserByChat;
import org.tyniest.user.entity.User;

import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Select;


@Dao
public interface UserRepository {
    @Select
    MappedReactiveResultSet<UserByChat> findByChatId(final UUID chatId);
    
    @Select(customWhereClause = "id in :userIds")
    MappedReactiveResultSet<User> findAllByIds(final List<UUID> userIds);

    @Select(customWhereClause = "name LIKE :preparedQuery", allowFiltering = true)
    PagingIterable<User> findByName(final String preparedQuery);
}
