package org.tyniest.chat.repository;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.BoundStatementBuilder;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.saving.NullSavingStrategy;
import com.datastax.oss.driver.internal.core.util.concurrent.BlockingOperation;
import com.datastax.oss.driver.internal.core.util.concurrent.CompletableFutures;
import com.datastax.oss.driver.internal.mapper.DaoBase;
import java.lang.Boolean;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
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
import org.tyniest.chat.entity.Chat;
import org.tyniest.chat.entity.ChatByUser;
import org.tyniest.chat.entity.ChatByUserHelper__MapperGenerated;
import org.tyniest.chat.entity.ChatHelper__MapperGenerated;
import org.tyniest.chat.entity.ChatUserCursor;
import org.tyniest.chat.entity.ChatUserCursorHelper__MapperGenerated;
import org.tyniest.chat.entity.ChatUserSettings;
import org.tyniest.chat.entity.ChatUserSettingsHelper__MapperGenerated;
import org.tyniest.chat.entity.Reaction;
import org.tyniest.chat.entity.ReactionHelper__MapperGenerated;
import org.tyniest.chat.entity.UserByChat;
import org.tyniest.chat.entity.UserByChatHelper__MapperGenerated;

/**
 * Generated by the DataStax driver mapper, do not edit directly.
 */
@SuppressWarnings("all")
public class ChatRepositoryImpl__MapperGenerated extends DaoBase implements ChatRepository {
  private static final GenericType<List<UUID>> GENERIC_TYPE = new GenericType<List<UUID>>(){};

  private static final Logger LOG = LoggerFactory.getLogger(ChatRepositoryImpl__MapperGenerated.class);

  private final ChatHelper__MapperGenerated chatHelper;

  private final ChatUserSettingsHelper__MapperGenerated chatUserSettingsHelper;

  private final UserByChatHelper__MapperGenerated userByChatHelper;

  private final ChatByUserHelper__MapperGenerated chatByUserHelper;

  private final ChatUserCursorHelper__MapperGenerated chatUserCursorHelper;

  private final ReactionHelper__MapperGenerated reactionHelper;

  private final PreparedStatement findByIdStatement;

  private final PreparedStatement saveStatement;

  private final PreparedStatement saveStatement1;

  private final PreparedStatement deleteStatement;

  private final PreparedStatement saveStatement2;

  private final PreparedStatement saveStatement3;

  private final PreparedStatement saveStatement4;

  private final PreparedStatement deleteStatement1;

  private final PreparedStatement saveStatement5;

  private final PreparedStatement deleteStatement2;

  private final PreparedStatement deleteStatement3;

  private final PreparedStatement deleteStatement4;

  private final PreparedStatement deleteStatement5;

  private final PreparedStatement findByUserIdStatement;

  private final PreparedStatement findAllByIdsStatement;

  private final PreparedStatement findBySignalIdStatement;

  private final PreparedStatement countByChatIdAndUserIdStatement;

  private final PreparedStatement findByChatIdAndUserIdStatement;

  private final PreparedStatement findByChatIdStatement;

  private ChatRepositoryImpl__MapperGenerated(MapperContext context,
      ChatHelper__MapperGenerated chatHelper,
      ChatUserSettingsHelper__MapperGenerated chatUserSettingsHelper,
      UserByChatHelper__MapperGenerated userByChatHelper,
      ChatByUserHelper__MapperGenerated chatByUserHelper,
      ChatUserCursorHelper__MapperGenerated chatUserCursorHelper,
      ReactionHelper__MapperGenerated reactionHelper, PreparedStatement findByIdStatement,
      PreparedStatement saveStatement, PreparedStatement saveStatement1,
      PreparedStatement deleteStatement, PreparedStatement saveStatement2,
      PreparedStatement saveStatement3, PreparedStatement saveStatement4,
      PreparedStatement deleteStatement1, PreparedStatement saveStatement5,
      PreparedStatement deleteStatement2, PreparedStatement deleteStatement3,
      PreparedStatement deleteStatement4, PreparedStatement deleteStatement5,
      PreparedStatement findByUserIdStatement, PreparedStatement findAllByIdsStatement,
      PreparedStatement findBySignalIdStatement, PreparedStatement countByChatIdAndUserIdStatement,
      PreparedStatement findByChatIdAndUserIdStatement, PreparedStatement findByChatIdStatement) {
    super(context);
    this.chatHelper = chatHelper;
    this.chatUserSettingsHelper = chatUserSettingsHelper;
    this.userByChatHelper = userByChatHelper;
    this.chatByUserHelper = chatByUserHelper;
    this.chatUserCursorHelper = chatUserCursorHelper;
    this.reactionHelper = reactionHelper;
    this.findByIdStatement = findByIdStatement;
    this.saveStatement = saveStatement;
    this.saveStatement1 = saveStatement1;
    this.deleteStatement = deleteStatement;
    this.saveStatement2 = saveStatement2;
    this.saveStatement3 = saveStatement3;
    this.saveStatement4 = saveStatement4;
    this.deleteStatement1 = deleteStatement1;
    this.saveStatement5 = saveStatement5;
    this.deleteStatement2 = deleteStatement2;
    this.deleteStatement3 = deleteStatement3;
    this.deleteStatement4 = deleteStatement4;
    this.deleteStatement5 = deleteStatement5;
    this.findByUserIdStatement = findByUserIdStatement;
    this.findAllByIdsStatement = findAllByIdsStatement;
    this.findBySignalIdStatement = findBySignalIdStatement;
    this.countByChatIdAndUserIdStatement = countByChatIdAndUserIdStatement;
    this.findByChatIdAndUserIdStatement = findByChatIdAndUserIdStatement;
    this.findByChatIdStatement = findByChatIdStatement;
  }

  @Override
  public Optional<Chat> findById(UUID productId) {
    BoundStatementBuilder boundStatementBuilder = findByIdStatement.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("id", productId, UUID.class);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return executeAndMapToOptionalEntity(boundStatement, chatHelper);
  }

  @Override
  public BoundStatement save(Chat product) {
    BoundStatementBuilder boundStatementBuilder = saveStatement.boundStatementBuilder();
    chatHelper.set(product, boundStatementBuilder, NullSavingStrategy.DO_NOT_SET, false);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return boundStatement;
  }

  @Override
  public BoundStatement save(ChatUserSettings settings) {
    BoundStatementBuilder boundStatementBuilder = saveStatement1.boundStatementBuilder();
    chatUserSettingsHelper.set(settings, boundStatementBuilder, NullSavingStrategy.DO_NOT_SET, false);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return boundStatement;
  }

  @Override
  public void delete(ChatUserSettings settings) {
    BoundStatementBuilder boundStatementBuilder = deleteStatement.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("chat_id", settings.getChatId(), UUID.class);
    boundStatementBuilder = boundStatementBuilder.set("user_id", settings.getUserId(), UUID.class);
    BoundStatement boundStatement = boundStatementBuilder.build();
    execute(boundStatement);
  }

  @Override
  public BoundStatement save(UserByChat product) {
    BoundStatementBuilder boundStatementBuilder = saveStatement2.boundStatementBuilder();
    userByChatHelper.set(product, boundStatementBuilder, NullSavingStrategy.DO_NOT_SET, false);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return boundStatement;
  }

  @Override
  public BoundStatement save(ChatByUser product) {
    BoundStatementBuilder boundStatementBuilder = saveStatement3.boundStatementBuilder();
    chatByUserHelper.set(product, boundStatementBuilder, NullSavingStrategy.DO_NOT_SET, false);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return boundStatement;
  }

  @Override
  public void save(ChatUserCursor cursor) {
    BoundStatementBuilder boundStatementBuilder = saveStatement4.boundStatementBuilder();
    chatUserCursorHelper.set(cursor, boundStatementBuilder, NullSavingStrategy.DO_NOT_SET, false);
    BoundStatement boundStatement = boundStatementBuilder.build();
    execute(boundStatement);
  }

  @Override
  public void delete(UUID chatId, UUID userId) {
    BoundStatementBuilder boundStatementBuilder = deleteStatement1.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("chatId", chatId, UUID.class);
    boundStatementBuilder = boundStatementBuilder.set("userId", userId, UUID.class);
    BoundStatement boundStatement = boundStatementBuilder.build();
    execute(boundStatement);
  }

  @Override
  public CompletionStage<Void> save(Reaction reaction) {
    try {
      BoundStatementBuilder boundStatementBuilder = saveStatement5.boundStatementBuilder();
      reactionHelper.set(reaction, boundStatementBuilder, NullSavingStrategy.DO_NOT_SET, false);
      BoundStatement boundStatement = boundStatementBuilder.build();
      return executeAsyncAndMapToVoid(boundStatement);
    } catch (Exception e) {
      return CompletableFutures.failedFuture(e);
    }
  }

  @Override
  public CompletionStage<Void> delete(Reaction reaction) {
    try {
      BoundStatementBuilder boundStatementBuilder = deleteStatement2.boundStatementBuilder();
      boundStatementBuilder = boundStatementBuilder.set("signal_id", reaction.getSignalId(), UUID.class);
      boundStatementBuilder = boundStatementBuilder.set("user_id", reaction.getUserId(), UUID.class);
      boundStatementBuilder = boundStatementBuilder.set("value", reaction.getValue(), String.class);
      BoundStatement boundStatement = boundStatementBuilder.build();
      return executeAsyncAndMapToVoid(boundStatement);
    } catch (Exception e) {
      return CompletableFutures.failedFuture(e);
    }
  }

  @Override
  public BoundStatement delete(UserByChat item) {
    BoundStatementBuilder boundStatementBuilder = deleteStatement3.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("chat_id", item.getChatId(), UUID.class);
    boundStatementBuilder = boundStatementBuilder.set("user_id", item.getUserId(), UUID.class);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return boundStatement;
  }

  @Override
  public BoundStatement delete(ChatByUser item) {
    BoundStatementBuilder boundStatementBuilder = deleteStatement4.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("user_id", item.getUserId(), UUID.class);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return boundStatement;
  }

  @Override
  public void delete(Chat chat) {
    BoundStatementBuilder boundStatementBuilder = deleteStatement5.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("id", chat.getId(), UUID.class);
    BoundStatement boundStatement = boundStatementBuilder.build();
    execute(boundStatement);
  }

  @Override
  public PagingIterable<ChatByUser> findByUserId(UUID userId) {
    BoundStatementBuilder boundStatementBuilder = findByUserIdStatement.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("user_id", userId, UUID.class);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return executeAndMapToEntityIterable(boundStatement, chatByUserHelper);
  }

  @Override
  public PagingIterable<Chat> findAllByIds(List<UUID> chatIds) {
    BoundStatementBuilder boundStatementBuilder = findAllByIdsStatement.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("chatIds", chatIds, GENERIC_TYPE);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return executeAndMapToEntityIterable(boundStatement, chatHelper);
  }

  @Override
  public PagingIterable<Reaction> findBySignalId(List<UUID> signalIds) {
    BoundStatementBuilder boundStatementBuilder = findBySignalIdStatement.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("signalIds", signalIds, GENERIC_TYPE);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return executeAndMapToEntityIterable(boundStatement, reactionHelper);
  }

  @Override
  public CompletionStage<Long> countByChatIdAndUserId(UUID chatId, UUID userId) {
    try {
      BoundStatementBuilder boundStatementBuilder = countByChatIdAndUserIdStatement.boundStatementBuilder();
      NullSavingStrategy nullSavingStrategy = NullSavingStrategy.DO_NOT_SET;
      if (chatId != null || nullSavingStrategy == NullSavingStrategy.SET_TO_NULL) {
        boundStatementBuilder = boundStatementBuilder.set("chatId", chatId, UUID.class);
      }
      if (userId != null || nullSavingStrategy == NullSavingStrategy.SET_TO_NULL) {
        boundStatementBuilder = boundStatementBuilder.set("userId", userId, UUID.class);
      }
      BoundStatement boundStatement = boundStatementBuilder.build();
      return executeAsyncAndMapFirstColumnToLong(boundStatement);
    } catch (Exception e) {
      return CompletableFutures.failedFuture(e);
    }
  }

  @Override
  public Optional<ChatUserSettings> findByChatIdAndUserId(UUID chatId, UUID userId) {
    BoundStatementBuilder boundStatementBuilder = findByChatIdAndUserIdStatement.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("chatId", chatId, UUID.class);
    boundStatementBuilder = boundStatementBuilder.set("userId", userId, UUID.class);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return executeAndMapToOptionalEntity(boundStatement, chatUserSettingsHelper);
  }

  @Override
  public PagingIterable<ChatUserCursor> findByChatId(UUID chatId) {
    BoundStatementBuilder boundStatementBuilder = findByChatIdStatement.boundStatementBuilder();
    boundStatementBuilder = boundStatementBuilder.set("chatId", chatId, UUID.class);
    BoundStatement boundStatement = boundStatementBuilder.build();
    return executeAndMapToEntityIterable(boundStatement, chatUserCursorHelper);
  }

  public static CompletableFuture<ChatRepository> initAsync(MapperContext context) {
    LOG.debug("[{}] Initializing new instance for keyspace = {} and table = {}",
        context.getSession().getName(),
        context.getKeyspaceId(),
        context.getTableId());
    throwIfProtocolVersionV3(context);
    try {
      // Initialize all entity helpers
      ChatHelper__MapperGenerated chatHelper = new ChatHelper__MapperGenerated(context);
      if ((Boolean)context.getCustomState().get("datastax.mapper.schemaValidationEnabled")) {
        chatHelper.validateEntityFields();
      }
      ChatUserSettingsHelper__MapperGenerated chatUserSettingsHelper = new ChatUserSettingsHelper__MapperGenerated(context);
      if ((Boolean)context.getCustomState().get("datastax.mapper.schemaValidationEnabled")) {
        chatUserSettingsHelper.validateEntityFields();
      }
      UserByChatHelper__MapperGenerated userByChatHelper = new UserByChatHelper__MapperGenerated(context);
      if ((Boolean)context.getCustomState().get("datastax.mapper.schemaValidationEnabled")) {
        userByChatHelper.validateEntityFields();
      }
      ChatByUserHelper__MapperGenerated chatByUserHelper = new ChatByUserHelper__MapperGenerated(context);
      if ((Boolean)context.getCustomState().get("datastax.mapper.schemaValidationEnabled")) {
        chatByUserHelper.validateEntityFields();
      }
      ChatUserCursorHelper__MapperGenerated chatUserCursorHelper = new ChatUserCursorHelper__MapperGenerated(context);
      if ((Boolean)context.getCustomState().get("datastax.mapper.schemaValidationEnabled")) {
        chatUserCursorHelper.validateEntityFields();
      }
      ReactionHelper__MapperGenerated reactionHelper = new ReactionHelper__MapperGenerated(context);
      if ((Boolean)context.getCustomState().get("datastax.mapper.schemaValidationEnabled")) {
        reactionHelper.validateEntityFields();
      }
      List<CompletionStage<PreparedStatement>> prepareStages = new ArrayList<>();
      // Prepare the statement for `public abstract Optional<org.tyniest.chat.entity.Chat> findById(java.util.UUID) `:
      SimpleStatement findByIdStatement_simple = chatHelper.selectByPrimaryKeyParts(1).build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract Optional<org.tyniest.chat.entity.Chat> findById(java.util.UUID) ",
          context.getSession().getName(),
          findByIdStatement_simple.getQuery());
      CompletionStage<PreparedStatement> findByIdStatement = prepare(findByIdStatement_simple, context);
      prepareStages.add(findByIdStatement);
      // Prepare the statement for `public abstract com.datastax.oss.driver.api.core.cql.BoundStatement save(org.tyniest.chat.entity.Chat) `:
      SimpleStatement saveStatement_simple = chatHelper.insert().build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract com.datastax.oss.driver.api.core.cql.BoundStatement save(org.tyniest.chat.entity.Chat) ",
          context.getSession().getName(),
          saveStatement_simple.getQuery());
      CompletionStage<PreparedStatement> saveStatement = prepare(saveStatement_simple, context);
      prepareStages.add(saveStatement);
      // Prepare the statement for `public abstract com.datastax.oss.driver.api.core.cql.BoundStatement save(org.tyniest.chat.entity.ChatUserSettings) `:
      SimpleStatement saveStatement1_simple = chatUserSettingsHelper.insert().build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract com.datastax.oss.driver.api.core.cql.BoundStatement save(org.tyniest.chat.entity.ChatUserSettings) ",
          context.getSession().getName(),
          saveStatement1_simple.getQuery());
      CompletionStage<PreparedStatement> saveStatement1 = prepare(saveStatement1_simple, context);
      prepareStages.add(saveStatement1);
      // Prepare the statement for `public abstract void delete(org.tyniest.chat.entity.ChatUserSettings) `:
      SimpleStatement deleteStatement_simple = chatUserSettingsHelper.deleteByPrimaryKeyParts(2).build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract void delete(org.tyniest.chat.entity.ChatUserSettings) ",
          context.getSession().getName(),
          deleteStatement_simple.getQuery());
      CompletionStage<PreparedStatement> deleteStatement = prepare(deleteStatement_simple, context);
      prepareStages.add(deleteStatement);
      // Prepare the statement for `public abstract com.datastax.oss.driver.api.core.cql.BoundStatement save(org.tyniest.chat.entity.UserByChat) `:
      SimpleStatement saveStatement2_simple = userByChatHelper.insert().build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract com.datastax.oss.driver.api.core.cql.BoundStatement save(org.tyniest.chat.entity.UserByChat) ",
          context.getSession().getName(),
          saveStatement2_simple.getQuery());
      CompletionStage<PreparedStatement> saveStatement2 = prepare(saveStatement2_simple, context);
      prepareStages.add(saveStatement2);
      // Prepare the statement for `public abstract com.datastax.oss.driver.api.core.cql.BoundStatement save(org.tyniest.chat.entity.ChatByUser) `:
      SimpleStatement saveStatement3_simple = chatByUserHelper.insert().build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract com.datastax.oss.driver.api.core.cql.BoundStatement save(org.tyniest.chat.entity.ChatByUser) ",
          context.getSession().getName(),
          saveStatement3_simple.getQuery());
      CompletionStage<PreparedStatement> saveStatement3 = prepare(saveStatement3_simple, context);
      prepareStages.add(saveStatement3);
      // Prepare the statement for `public abstract void save(org.tyniest.chat.entity.ChatUserCursor) `:
      SimpleStatement saveStatement4_simple = chatUserCursorHelper.insert().ifNotExists().build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract void save(org.tyniest.chat.entity.ChatUserCursor) ",
          context.getSession().getName(),
          saveStatement4_simple.getQuery());
      CompletionStage<PreparedStatement> saveStatement4 = prepare(saveStatement4_simple, context);
      prepareStages.add(saveStatement4);
      // Prepare the statement for `public abstract void delete(java.util.UUID, java.util.UUID) `:
      SimpleStatement deleteStatement1_simple = chatUserCursorHelper.deleteStart().whereRaw("chat_id = :chatId and user_id = :userId").build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract void delete(java.util.UUID, java.util.UUID) ",
          context.getSession().getName(),
          deleteStatement1_simple.getQuery());
      CompletionStage<PreparedStatement> deleteStatement1 = prepare(deleteStatement1_simple, context);
      prepareStages.add(deleteStatement1);
      // Prepare the statement for `public abstract CompletionStage<java.lang.Void> save(org.tyniest.chat.entity.Reaction) `:
      SimpleStatement saveStatement5_simple = reactionHelper.insert().ifNotExists().build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract CompletionStage<java.lang.Void> save(org.tyniest.chat.entity.Reaction) ",
          context.getSession().getName(),
          saveStatement5_simple.getQuery());
      CompletionStage<PreparedStatement> saveStatement5 = prepare(saveStatement5_simple, context);
      prepareStages.add(saveStatement5);
      // Prepare the statement for `public abstract CompletionStage<java.lang.Void> delete(org.tyniest.chat.entity.Reaction) `:
      SimpleStatement deleteStatement2_simple = reactionHelper.deleteByPrimaryKeyParts(3).build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract CompletionStage<java.lang.Void> delete(org.tyniest.chat.entity.Reaction) ",
          context.getSession().getName(),
          deleteStatement2_simple.getQuery());
      CompletionStage<PreparedStatement> deleteStatement2 = prepare(deleteStatement2_simple, context);
      prepareStages.add(deleteStatement2);
      // Prepare the statement for `public abstract com.datastax.oss.driver.api.core.cql.BoundStatement delete(org.tyniest.chat.entity.UserByChat) `:
      SimpleStatement deleteStatement3_simple = userByChatHelper.deleteByPrimaryKeyParts(2).build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract com.datastax.oss.driver.api.core.cql.BoundStatement delete(org.tyniest.chat.entity.UserByChat) ",
          context.getSession().getName(),
          deleteStatement3_simple.getQuery());
      CompletionStage<PreparedStatement> deleteStatement3 = prepare(deleteStatement3_simple, context);
      prepareStages.add(deleteStatement3);
      // Prepare the statement for `public abstract com.datastax.oss.driver.api.core.cql.BoundStatement delete(org.tyniest.chat.entity.ChatByUser) `:
      SimpleStatement deleteStatement4_simple = chatByUserHelper.deleteByPrimaryKeyParts(1).build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract com.datastax.oss.driver.api.core.cql.BoundStatement delete(org.tyniest.chat.entity.ChatByUser) ",
          context.getSession().getName(),
          deleteStatement4_simple.getQuery());
      CompletionStage<PreparedStatement> deleteStatement4 = prepare(deleteStatement4_simple, context);
      prepareStages.add(deleteStatement4);
      // Prepare the statement for `public abstract void delete(org.tyniest.chat.entity.Chat) `:
      SimpleStatement deleteStatement5_simple = chatHelper.deleteByPrimaryKeyParts(1).build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract void delete(org.tyniest.chat.entity.Chat) ",
          context.getSession().getName(),
          deleteStatement5_simple.getQuery());
      CompletionStage<PreparedStatement> deleteStatement5 = prepare(deleteStatement5_simple, context);
      prepareStages.add(deleteStatement5);
      // Prepare the statement for `public abstract PagingIterable<org.tyniest.chat.entity.ChatByUser> findByUserId(java.util.UUID) `:
      SimpleStatement findByUserIdStatement_simple = chatByUserHelper.selectByPrimaryKeyParts(1).build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract PagingIterable<org.tyniest.chat.entity.ChatByUser> findByUserId(java.util.UUID) ",
          context.getSession().getName(),
          findByUserIdStatement_simple.getQuery());
      CompletionStage<PreparedStatement> findByUserIdStatement = prepare(findByUserIdStatement_simple, context);
      prepareStages.add(findByUserIdStatement);
      // Prepare the statement for `public abstract PagingIterable<org.tyniest.chat.entity.Chat> findAllByIds(List<java.util.UUID>) `:
      SimpleStatement findAllByIdsStatement_simple = chatHelper.selectStart().whereRaw("id in :chatIds").build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract PagingIterable<org.tyniest.chat.entity.Chat> findAllByIds(List<java.util.UUID>) ",
          context.getSession().getName(),
          findAllByIdsStatement_simple.getQuery());
      CompletionStage<PreparedStatement> findAllByIdsStatement = prepare(findAllByIdsStatement_simple, context);
      prepareStages.add(findAllByIdsStatement);
      // Prepare the statement for `public abstract PagingIterable<org.tyniest.chat.entity.Reaction> findBySignalId(List<java.util.UUID>) `:
      SimpleStatement findBySignalIdStatement_simple = reactionHelper.selectStart().whereRaw("signal_id in :signalIds").build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract PagingIterable<org.tyniest.chat.entity.Reaction> findBySignalId(List<java.util.UUID>) ",
          context.getSession().getName(),
          findBySignalIdStatement_simple.getQuery());
      CompletionStage<PreparedStatement> findBySignalIdStatement = prepare(findBySignalIdStatement_simple, context);
      prepareStages.add(findBySignalIdStatement);
      // Prepare the statement for `public abstract CompletionStage<java.lang.Long> countByChatIdAndUserId(java.util.UUID, java.util.UUID) `:
      SimpleStatement countByChatIdAndUserIdStatement_simple = replaceKeyspaceAndTablePlaceholders("select count(*) from user_by_chat where chat_id = :chatId and user_id = :userId", context, null);
      LOG.debug("[{}] Preparing query `{}` for method public abstract CompletionStage<java.lang.Long> countByChatIdAndUserId(java.util.UUID, java.util.UUID) ",
          context.getSession().getName(),
          countByChatIdAndUserIdStatement_simple.getQuery());
      CompletionStage<PreparedStatement> countByChatIdAndUserIdStatement = prepare(countByChatIdAndUserIdStatement_simple, context);
      prepareStages.add(countByChatIdAndUserIdStatement);
      // Prepare the statement for `public abstract Optional<org.tyniest.chat.entity.ChatUserSettings> findByChatIdAndUserId(java.util.UUID, java.util.UUID) `:
      SimpleStatement findByChatIdAndUserIdStatement_simple = chatUserSettingsHelper.selectStart().whereRaw("chat_id = :chatId and user_id = :userId").build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract Optional<org.tyniest.chat.entity.ChatUserSettings> findByChatIdAndUserId(java.util.UUID, java.util.UUID) ",
          context.getSession().getName(),
          findByChatIdAndUserIdStatement_simple.getQuery());
      CompletionStage<PreparedStatement> findByChatIdAndUserIdStatement = prepare(findByChatIdAndUserIdStatement_simple, context);
      prepareStages.add(findByChatIdAndUserIdStatement);
      // Prepare the statement for `public abstract PagingIterable<org.tyniest.chat.entity.ChatUserCursor> findByChatId(java.util.UUID) `:
      SimpleStatement findByChatIdStatement_simple = chatUserCursorHelper.selectStart().whereRaw("chat_id = :chatId").build();
      LOG.debug("[{}] Preparing query `{}` for method public abstract PagingIterable<org.tyniest.chat.entity.ChatUserCursor> findByChatId(java.util.UUID) ",
          context.getSession().getName(),
          findByChatIdStatement_simple.getQuery());
      CompletionStage<PreparedStatement> findByChatIdStatement = prepare(findByChatIdStatement_simple, context);
      prepareStages.add(findByChatIdStatement);
      // Initialize all method invokers
      // Build the DAO when all statements are prepared
      return CompletableFutures.allSuccessful(prepareStages)
          .thenApply(v -> (ChatRepository) new ChatRepositoryImpl__MapperGenerated(context,
              chatHelper,
              chatUserSettingsHelper,
              userByChatHelper,
              chatByUserHelper,
              chatUserCursorHelper,
              reactionHelper,
              CompletableFutures.getCompleted(findByIdStatement),
              CompletableFutures.getCompleted(saveStatement),
              CompletableFutures.getCompleted(saveStatement1),
              CompletableFutures.getCompleted(deleteStatement),
              CompletableFutures.getCompleted(saveStatement2),
              CompletableFutures.getCompleted(saveStatement3),
              CompletableFutures.getCompleted(saveStatement4),
              CompletableFutures.getCompleted(deleteStatement1),
              CompletableFutures.getCompleted(saveStatement5),
              CompletableFutures.getCompleted(deleteStatement2),
              CompletableFutures.getCompleted(deleteStatement3),
              CompletableFutures.getCompleted(deleteStatement4),
              CompletableFutures.getCompleted(deleteStatement5),
              CompletableFutures.getCompleted(findByUserIdStatement),
              CompletableFutures.getCompleted(findAllByIdsStatement),
              CompletableFutures.getCompleted(findBySignalIdStatement),
              CompletableFutures.getCompleted(countByChatIdAndUserIdStatement),
              CompletableFutures.getCompleted(findByChatIdAndUserIdStatement),
              CompletableFutures.getCompleted(findByChatIdStatement)))
          .toCompletableFuture();
    } catch (Throwable t) {
      return CompletableFutures.failedFuture(t);
    }
  }

  public static ChatRepository init(MapperContext context) {
    BlockingOperation.checkNotDriverThread();
    return CompletableFutures.getUninterruptibly(initAsync(context));
  }
}
