package org.tyniest.chat.mapper.entity;

import com.datastax.oss.driver.internal.core.util.concurrent.LazyReference;
import com.datastax.oss.driver.internal.mapper.DefaultMapperContext;
import java.lang.Override;
import java.lang.SuppressWarnings;
import org.tyniest.chat.repository.SignalRepository;
import org.tyniest.chat.repository.SignalRepositoryImpl__MapperGenerated;

/**
 * Do not instantiate this class directly, use {@link DatabaseMessageMapperBuilder} instead.
 *
 * <p>Generated by the DataStax driver mapper, do not edit directly.
 */
@SuppressWarnings("all")
public class DatabaseMessageMapperImpl__MapperGenerated implements DatabaseMessageMapper {
  private final DefaultMapperContext context;

  private final LazyReference<SignalRepository> repositoryCache;

  public DatabaseMessageMapperImpl__MapperGenerated(DefaultMapperContext context) {
    this.context = context;
    this.repositoryCache = new LazyReference<>(() -> SignalRepositoryImpl__MapperGenerated.init(context));
  }

  @Override
  public SignalRepository repository() {
    return repositoryCache.get();
  }
}
