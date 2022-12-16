package org.tyniest.user.mapper;

import org.tyniest.user.repository.UserRepository;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

@Mapper
public interface UserMapper {
    @DaoFactory
    UserRepository repository();
}
