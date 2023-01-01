package org.tyniest.user.repository;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.BoundStatementBuilder;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.internal.core.util.concurrent.BlockingOperation;
import com.datastax.oss.driver.internal.core.util.concurrent.CompletableFutures;
import com.datastax.oss.driver.internal.mapper.DaoBase;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.Throwable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tyniest.chat.entity.UserByChat;
import org.tyniest.chat.entity.UserByChatHelper__MapperGenerated;
import org.tyniest.user.entity.User;
import org.tyniest.user.entity.UserHelper__MapperGenerated;

/**
 * Generated by the DataStax driver mapper, do not edit directly.
 */
@SuppressWarnings("all")
public class UserRepositoryImpl__MapperGenerated extends DaoBase implements UserRepository {
  private static final GenericType<List<UUID>> GENERIC_TYPE = new GenericType<List<UUID>>(){};

  private static final Logger LOG = LoggerFactory.getLogger(UserRepositoryImpl__MapperGenerated.class);

  private final UserByChatHelper__MapperGenerated userByChatHelper;

  private final UserHelper__MapperGenerated userHelper;

  private final PreparedStatement findByChatIdStatement;

  private final PreparedStatement findAllByIdsStatement;

  private final PreparedStatement findByNameStatement;

  private UserRepositoryImpl__MapperGenerated(MapperContext context,
      UserByChatHelper__MapperGenerated userByChatHelper, UserHelper__MapperGenerated userHelper,
      PreparedStatement findByChatIdStatement, PreparedStatement findAllByIdsStatement,
      PreparedStatement findByNameStatement) {
    super(context);
    this.userByChatHelper = userByChatHelper;
    this.userHelper = userHelper;
    this.findByChatIdStatement = findByChatIdStatement;
    this.findAllByIdsStatement = findAllByIdsStatement;
    this.findByNameStatement = findByNameStatement;
  }

  @Override
  public PagingIterable<UserByChat> findByChatId(UUID chatId) {
    BoundStatementBuilder boundStatementBuilder = findByChatIdStatement.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("chat_id", chatId, UUID.class);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return executeAndMapToEntityIterable(boundStatement, userByChatHelper);
  }

  @Override
  public PagingIterable<User> findAllByIds(List<UUID> userIds) {
    BoundStatementBuilder boundStatementBuilder = findAllByIdsStatement.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("userIds", userIds, GENERIC_TYPE);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return executeAndMapToEntityIterable(boundStatement, userHelper);
  }

  @Override
  public PagingIterable<User> findByName(String preparedQuery) {
    BoundStatementBuilder boundStatementBuilder = findByNameStatement.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("preparedQuery", preparedQuery, String.class);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return executeAndMapToEntityIterable(boundStatement, userHelper);
  }

  public static CompletableFuture<UserRepository> initAsync(MapperContext context) {
    LOG.debug("[{}] Initializing new instance for keyspace = {} and table = {}",
        context.getSession().getName(),
        context.getKeyspaceId(),
        context.getTableId());
    try {
      // Initialize all entity helpers
      UserByChatHelper__MapperGenerated userByChatHelper = new UserByChatHelper__MapperGenerated(context);
      if ((Boolean)context.getCustomState().get("datastax.mapper.schemaValidationEnabled")) {
        userByChatHelper.validateEntityFields();
      }
      UserHelper__MapperGenerated userHelper = new UserHelper__MapperGenerated(context);
      if ((Boolean)context.getCustomState().get("datastax.mapper.schemaValidationEnabled")) {
        userHelper.validateEntityFields();
      }
      List<CompletionStage<PreparedStatement>> prepareStages = new ArrayList<>();
      // Prepare the statement for `findByChatId(java.util.UUID)`:
      SimpleStatement findByChatIdStatement_simple = userByChatHelper.selectByPrimaryKeyParts(1).build();
      LOG.debug("[{}] Preparing query `{}` for method findByChatId(java.util.UUID)",
          context.getSession().getName(),
          findByChatIdStatement_simple.getQuery());
      CompletionStage<PreparedStatement> findByChatIdStatement = prepare(findByChatIdStatement_simple, context);
      prepareStages.add(findByChatIdStatement);
      // Prepare the statement for `findAllByIds(java.util.List<java.util.UUID>)`:
      SimpleStatement findAllByIdsStatement_simple = userHelper.selectStart().whereRaw("id in :userIds").build();
      LOG.debug("[{}] Preparing query `{}` for method findAllByIds(java.util.List<java.util.UUID>)",
          context.getSession().getName(),
          findAllByIdsStatement_simple.getQuery());
      CompletionStage<PreparedStatement> findAllByIdsStatement = prepare(findAllByIdsStatement_simple, context);
      prepareStages.add(findAllByIdsStatement);
      // Prepare the statement for `findByName(java.lang.String)`:
      SimpleStatement findByNameStatement_simple = userHelper.selectStart().whereRaw("name like :preparedQuery").allowFiltering().build();
      LOG.debug("[{}] Preparing query `{}` for method findByName(java.lang.String)",
          context.getSession().getName(),
          findByNameStatement_simple.getQuery());
      CompletionStage<PreparedStatement> findByNameStatement = prepare(findByNameStatement_simple, context);
      prepareStages.add(findByNameStatement);
      // Initialize all method invokers
      // Build the DAO when all statements are prepared
      return CompletableFutures.allSuccessful(prepareStages)
          .thenApply(v -> (UserRepository) new UserRepositoryImpl__MapperGenerated(context,
              userByChatHelper,
              userHelper,
              CompletableFutures.getCompleted(findByChatIdStatement),
              CompletableFutures.getCompleted(findAllByIdsStatement),
              CompletableFutures.getCompleted(findByNameStatement)))
          .toCompletableFuture();
    } catch (Throwable t) {
      return CompletableFutures.failedFuture(t);
    }
  }

  public static UserRepository init(MapperContext context) {
    BlockingOperation.checkNotDriverThread();
    return CompletableFutures.getUninterruptibly(initAsync(context));
  }
}
