package org.tyniest.chat.repository;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.BoundStatementBuilder;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.saving.NullSavingStrategy;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.internal.core.util.concurrent.BlockingOperation;
import com.datastax.oss.driver.internal.core.util.concurrent.CompletableFutures;
import com.datastax.oss.driver.internal.mapper.DaoBase;
import java.lang.Boolean;
import java.lang.Exception;
import java.lang.Override;
import java.lang.SuppressWarnings;
import java.lang.Throwable;
import java.lang.Void;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tyniest.chat.entity.Signal;
import org.tyniest.chat.entity.SignalHelper__MapperGenerated;

/**
 * Generated by the DataStax driver mapper, do not edit directly.
 */
@SuppressWarnings("all")
public class SignalRepositoryImpl__MapperGenerated extends DaoBase implements SignalRepository {
  private static final GenericType<List<UUID>> GENERIC_TYPE = new GenericType<List<UUID>>(){};

  private static final Logger LOG = LoggerFactory.getLogger(SignalRepositoryImpl__MapperGenerated.class);

  private final SignalHelper__MapperGenerated signalHelper;

  private final PreparedStatement saveStatement;

  private final PreparedStatement findByChatIdAndCreatedAtStatement;

  private final PreparedStatement findByChatIdStatement;

  private final PreparedStatement findByChatIdAndOffsetStatement;

  private final PreparedStatement findAllByIdsStatement;

  private SignalRepositoryImpl__MapperGenerated(MapperContext context,
      SignalHelper__MapperGenerated signalHelper, PreparedStatement saveStatement,
      PreparedStatement findByChatIdAndCreatedAtStatement, PreparedStatement findByChatIdStatement,
      PreparedStatement findByChatIdAndOffsetStatement, PreparedStatement findAllByIdsStatement) {
    super(context);
    this.signalHelper = signalHelper;
    this.saveStatement = saveStatement;
    this.findByChatIdAndCreatedAtStatement = findByChatIdAndCreatedAtStatement;
    this.findByChatIdStatement = findByChatIdStatement;
    this.findByChatIdAndOffsetStatement = findByChatIdAndOffsetStatement;
    this.findAllByIdsStatement = findAllByIdsStatement;
  }

  @Override
  public CompletionStage<Void> save(Signal message) {
    try {
      BoundStatementBuilder boundStatementBuilder = saveStatement.boundStatementBuilder();
      signalHelper.set(message, boundStatementBuilder, NullSavingStrategy.DO_NOT_SET, false);
      BoundStatement boundStatement = boundStatementBuilder.build();
      return executeAsyncAndMapToVoid(boundStatement);
    } catch (Exception e) {
      return CompletableFutures.failedFuture(e);
    }
  }

  @Override
  public Optional<Signal> findByChatIdAndCreatedAt(UUID chatId, UUID createdAt) {
    BoundStatementBuilder boundStatementBuilder = findByChatIdAndCreatedAtStatement.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("chatId", chatId, UUID.class);
    boundStatementBuilder = boundStatementBuilder.set("createdAt", createdAt, UUID.class);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return executeAndMapToOptionalEntity(boundStatement, signalHelper);
  }

  @Override
  public PagingIterable<Signal> findByChatId(UUID chatId, int l) {
    BoundStatementBuilder boundStatementBuilder = findByChatIdStatement.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("chatId", chatId, UUID.class);
    boundStatementBuilder = boundStatementBuilder.setInt("l", l);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return executeAndMapToEntityIterable(boundStatement, signalHelper);
  }

  @Override
  public PagingIterable<Signal> findByChatIdAndOffset(UUID chatId, UUID offset, int l) {
    BoundStatementBuilder boundStatementBuilder = findByChatIdAndOffsetStatement.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("chatId", chatId, UUID.class);
    boundStatementBuilder = boundStatementBuilder.set("offset", offset, UUID.class);
    boundStatementBuilder = boundStatementBuilder.setInt("l", l);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return executeAndMapToEntityIterable(boundStatement, signalHelper);
  }

  @Override
  public PagingIterable<Signal> findAllByIds(UUID chatId, List<UUID> createdAtIds) {
    BoundStatementBuilder boundStatementBuilder = findAllByIdsStatement.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("chatId", chatId, UUID.class);
    boundStatementBuilder = boundStatementBuilder.set("createdAtIds", createdAtIds, GENERIC_TYPE);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return executeAndMapToEntityIterable(boundStatement, signalHelper);
  }

  public static CompletableFuture<SignalRepository> initAsync(MapperContext context) {
    LOG.debug("[{}] Initializing new instance for keyspace = {} and table = {}",
        context.getSession().getName(),
        context.getKeyspaceId(),
        context.getTableId());
    throwIfProtocolVersionV3(context);
    try {
      // Initialize all entity helpers
      SignalHelper__MapperGenerated signalHelper = new SignalHelper__MapperGenerated(context);
      if ((Boolean)context.getCustomState().get("datastax.mapper.schemaValidationEnabled")) {
        signalHelper.validateEntityFields();
      }
      List<CompletionStage<PreparedStatement>> prepareStages = new ArrayList<>();
      // Prepare the statement for `save(org.tyniest.chat.entity.Signal)`:
      SimpleStatement saveStatement_simple = signalHelper.insert().build();
      LOG.debug("[{}] Preparing query `{}` for method save(org.tyniest.chat.entity.Signal)",
          context.getSession().getName(),
          saveStatement_simple.getQuery());
      CompletionStage<PreparedStatement> saveStatement = prepare(saveStatement_simple, context);
      prepareStages.add(saveStatement);
      // Prepare the statement for `findByChatIdAndCreatedAt(java.util.UUID,java.util.UUID)`:
      SimpleStatement findByChatIdAndCreatedAtStatement_simple = signalHelper.selectStart().whereRaw("chat_id = :chatId and created_at = :createdAt").build();
      LOG.debug("[{}] Preparing query `{}` for method findByChatIdAndCreatedAt(java.util.UUID,java.util.UUID)",
          context.getSession().getName(),
          findByChatIdAndCreatedAtStatement_simple.getQuery());
      CompletionStage<PreparedStatement> findByChatIdAndCreatedAtStatement = prepare(findByChatIdAndCreatedAtStatement_simple, context);
      prepareStages.add(findByChatIdAndCreatedAtStatement);
      // Prepare the statement for `findByChatId(java.util.UUID,int)`:
      SimpleStatement findByChatIdStatement_simple = signalHelper.selectStart().whereRaw("chat_id = :chatId").limit(QueryBuilder.bindMarker("l")).build();
      LOG.debug("[{}] Preparing query `{}` for method findByChatId(java.util.UUID,int)",
          context.getSession().getName(),
          findByChatIdStatement_simple.getQuery());
      CompletionStage<PreparedStatement> findByChatIdStatement = prepare(findByChatIdStatement_simple, context);
      prepareStages.add(findByChatIdStatement);
      // Prepare the statement for `findByChatIdAndOffset(java.util.UUID,java.util.UUID,int)`:
      SimpleStatement findByChatIdAndOffsetStatement_simple = signalHelper.selectStart().whereRaw("chat_id = :chatId and created_at < :offset").limit(QueryBuilder.bindMarker("l")).build();
      LOG.debug("[{}] Preparing query `{}` for method findByChatIdAndOffset(java.util.UUID,java.util.UUID,int)",
          context.getSession().getName(),
          findByChatIdAndOffsetStatement_simple.getQuery());
      CompletionStage<PreparedStatement> findByChatIdAndOffsetStatement = prepare(findByChatIdAndOffsetStatement_simple, context);
      prepareStages.add(findByChatIdAndOffsetStatement);
      // Prepare the statement for `findAllByIds(java.util.UUID,java.util.List<java.util.UUID>)`:
      SimpleStatement findAllByIdsStatement_simple = signalHelper.selectStart().whereRaw("chat_id = :chatId and created_at in :createdAtIds").build();
      LOG.debug("[{}] Preparing query `{}` for method findAllByIds(java.util.UUID,java.util.List<java.util.UUID>)",
          context.getSession().getName(),
          findAllByIdsStatement_simple.getQuery());
      CompletionStage<PreparedStatement> findAllByIdsStatement = prepare(findAllByIdsStatement_simple, context);
      prepareStages.add(findAllByIdsStatement);
      // Initialize all method invokers
      // Build the DAO when all statements are prepared
      return CompletableFutures.allSuccessful(prepareStages)
          .thenApply(v -> (SignalRepository) new SignalRepositoryImpl__MapperGenerated(context,
              signalHelper,
              CompletableFutures.getCompleted(saveStatement),
              CompletableFutures.getCompleted(findByChatIdAndCreatedAtStatement),
              CompletableFutures.getCompleted(findByChatIdStatement),
              CompletableFutures.getCompleted(findByChatIdAndOffsetStatement),
              CompletableFutures.getCompleted(findAllByIdsStatement)))
          .toCompletableFuture();
    } catch (Throwable t) {
      return CompletableFutures.failedFuture(t);
    }
  }

  public static SignalRepository init(MapperContext context) {
    BlockingOperation.checkNotDriverThread();
    return CompletableFutures.getUninterruptibly(initAsync(context));
  }
}
