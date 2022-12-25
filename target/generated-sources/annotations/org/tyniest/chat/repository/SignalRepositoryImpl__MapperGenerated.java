package org.tyniest.chat.repository;

import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.BoundStatementBuilder;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.saving.NullSavingStrategy;
import com.datastax.oss.driver.api.mapper.result.MapperResultProducer;
import com.datastax.oss.driver.internal.core.util.concurrent.BlockingOperation;
import com.datastax.oss.driver.internal.core.util.concurrent.CompletableFutures;
import com.datastax.oss.driver.internal.mapper.DaoBase;
import com.datastax.oss.driver.shaded.guava.common.base.Throwables;
import java.lang.Boolean;
import java.lang.Exception;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.SuppressWarnings;
import java.lang.Throwable;
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
  private static final GenericType<List<Signal>> GENERIC_TYPE = new GenericType<List<Signal>>(){};

  private static final Logger LOG = LoggerFactory.getLogger(SignalRepositoryImpl__MapperGenerated.class);

  private final SignalHelper__MapperGenerated signalHelper;

  private final PreparedStatement saveStatement;

  private final PreparedStatement findByIdStatement;

  private final PreparedStatement findByChatIdAndOffsetStatement;

  private SignalRepositoryImpl__MapperGenerated(MapperContext context,
      SignalHelper__MapperGenerated signalHelper, PreparedStatement saveStatement,
      PreparedStatement findByIdStatement, PreparedStatement findByChatIdAndOffsetStatement) {
    super(context);
    this.signalHelper = signalHelper;
    this.saveStatement = saveStatement;
    this.findByIdStatement = findByIdStatement;
    this.findByChatIdAndOffsetStatement = findByChatIdAndOffsetStatement;
  }

  @Override
  public Signal save(Signal message) {
    BoundStatementBuilder boundStatementBuilder = saveStatement.boundStatementBuilder();
    signalHelper.set(message, boundStatementBuilder, NullSavingStrategy.DO_NOT_SET, false);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return executeAndMapToSingleEntity(boundStatement, signalHelper);
  }

  @Override
  public Optional<Signal> findById(UUID id) {
    BoundStatementBuilder boundStatementBuilder = findByIdStatement.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("id", id, UUID.class);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return executeAndMapToOptionalEntity(boundStatement, signalHelper);
  }

  @Override
  public List<Signal> findByChatIdAndOffset(UUID userId, int offset) {
    MapperResultProducer producer = context.getResultProducer(GENERIC_TYPE);
    try {
      BoundStatementBuilder boundStatementBuilder = findByChatIdAndOffsetStatement.boundStatementBuilder();
      boundStatementBuilder = boundStatementBuilder.set("id", userId, UUID.class);
      boundStatementBuilder = boundStatementBuilder.setInt("offset", offset);
      BoundStatement boundStatement = boundStatementBuilder.build();
      @SuppressWarnings("unchecked") List<Signal> result =
          (List<Signal>) producer.execute(boundStatement, context, signalHelper);
      return result;
    } catch (Exception e) {
      try {
        @SuppressWarnings("unchecked") List<Signal> result =
            (List<Signal>) producer.wrapError(e);
        return result;
      } catch (Exception e2) {
        Throwables.throwIfUnchecked(e2);
        throw new RuntimeException(e2);
      }
    }
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
      // Prepare the statement for `public abstract org.tyniest.chat.entity.Signal save(org.tyniest.chat.entity.Signal) `:
      SimpleStatement saveStatement_simple = signalHelper.insert().build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract org.tyniest.chat.entity.Signal save(org.tyniest.chat.entity.Signal) ",
          context.getSession().getName(),
          saveStatement_simple.getQuery());
      CompletionStage<PreparedStatement> saveStatement = prepare(saveStatement_simple, context);
      prepareStages.add(saveStatement);
      // Prepare the statement for `public abstract Optional<org.tyniest.chat.entity.Signal> findById(java.util.UUID) `:
      SimpleStatement findByIdStatement_simple = signalHelper.selectByPrimaryKeyParts(1).build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract Optional<org.tyniest.chat.entity.Signal> findById(java.util.UUID) ",
          context.getSession().getName(),
          findByIdStatement_simple.getQuery());
      CompletionStage<PreparedStatement> findByIdStatement = prepare(findByIdStatement_simple, context);
      prepareStages.add(findByIdStatement);
      // Prepare the statement for `public abstract List<org.tyniest.chat.entity.Signal> findByChatIdAndOffset(java.util.UUID, int) `:
      SimpleStatement findByChatIdAndOffsetStatement_simple = signalHelper.selectByPrimaryKeyParts(1).build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract List<org.tyniest.chat.entity.Signal> findByChatIdAndOffset(java.util.UUID, int) ",
          context.getSession().getName(),
          findByChatIdAndOffsetStatement_simple.getQuery());
      CompletionStage<PreparedStatement> findByChatIdAndOffsetStatement = prepare(findByChatIdAndOffsetStatement_simple, context);
      prepareStages.add(findByChatIdAndOffsetStatement);
      // Initialize all method invokers
      // Build the DAO when all statements are prepared
      return CompletableFutures.allSuccessful(prepareStages)
          .thenApply(v -> (SignalRepository) new SignalRepositoryImpl__MapperGenerated(context,
              signalHelper,
              CompletableFutures.getCompleted(saveStatement),
              CompletableFutures.getCompleted(findByIdStatement),
              CompletableFutures.getCompleted(findByChatIdAndOffsetStatement)))
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
