package org.tyniest.chat.mapper;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.mapper.MapperBuilder;
import com.datastax.oss.driver.internal.mapper.DefaultMapperContext;
import java.lang.Override;
import java.lang.SuppressWarnings;

import org.tyniest.chat.mapper.entity.MessageMapper;

/**
 * Builds an instance of {@link MessageMapper} wrapping a driver {@link CqlSession}.
 *
 * <p>Generated by the DataStax driver mapper, do not edit directly.
 */
@SuppressWarnings("all")
public class MessageMapperBuilder extends MapperBuilder<MessageMapper> {
  public MessageMapperBuilder(CqlSession session) {
    super(session);
  }

  @Override
  public MessageMapper build() {
    DefaultMapperContext context = new DefaultMapperContext(session, defaultKeyspaceId, defaultExecutionProfileName, defaultExecutionProfile, customState);
    return new MessageMapperImpl__MapperGenerated(context);
  }
}
