package org.tyniest.chat.entity;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.data.GettableByName;
import com.datastax.oss.driver.api.core.data.SettableByName;
import com.datastax.oss.driver.api.core.metadata.schema.KeyspaceMetadata;
import com.datastax.oss.driver.api.core.metadata.schema.TableMetadata;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.MapperException;
import com.datastax.oss.driver.api.mapper.entity.saving.NullSavingStrategy;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.delete.Delete;
import com.datastax.oss.driver.api.querybuilder.delete.DeleteSelection;
import com.datastax.oss.driver.api.querybuilder.insert.InsertInto;
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.select.SelectFrom;
import com.datastax.oss.driver.api.querybuilder.update.UpdateStart;
import com.datastax.oss.driver.internal.mapper.entity.EntityHelperBase;
import com.datastax.oss.driver.internal.querybuilder.update.DefaultUpdate;
import com.datastax.oss.driver.shaded.guava.common.collect.ImmutableList;
import java.lang.Class;
import java.lang.IllegalArgumentException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generated by the DataStax driver mapper, do not edit directly.
 */
@SuppressWarnings("all")
public class SignalHelper__MapperGenerated extends EntityHelperBase<Signal> {
  private static final Logger LOG = LoggerFactory.getLogger(SignalHelper__MapperGenerated.class);

  private static final GenericType<Instant> GENERIC_TYPE = new GenericType<Instant>(){};

  private static final GenericType<String> GENERIC_TYPE1 = new GenericType<String>(){};

  private static final GenericType<UUID> GENERIC_TYPE2 = new GenericType<UUID>(){};

  private final List<String> primaryKeys;

  public SignalHelper__MapperGenerated(MapperContext context) {
    super(context, "signal");
    LOG.debug("[{}] Entity Signal will be mapped to {}{}",
        context.getSession().getName(),
        getKeyspaceId() == null ? "" : getKeyspaceId() + ".",
        getTableId());
    this.primaryKeys = ImmutableList.<String>builder()
        .add("chat_id")
        .add("created_at")
        .build();
  }

  @Override
  public Class<Signal> getEntityClass() {
    return Signal.class;
  }

  @Override
  public <SettableT extends SettableByName<SettableT>> SettableT set(Signal entity,
      SettableT target, NullSavingStrategy nullSavingStrategy, boolean lenient) {
    if (!lenient || hasProperty(target, "chat_id")) {
      if (entity.getChatId() != null || nullSavingStrategy == NullSavingStrategy.SET_TO_NULL) {
        target = target.set("chat_id", entity.getChatId(), UUID.class);
      }
    }
    if (!lenient || hasProperty(target, "created_at")) {
      if (entity.getCreatedAt() != null || nullSavingStrategy == NullSavingStrategy.SET_TO_NULL) {
        target = target.set("created_at", entity.getCreatedAt(), Instant.class);
      }
    }
    if (!lenient || hasProperty(target, "user_id")) {
      if (entity.getUserId() != null || nullSavingStrategy == NullSavingStrategy.SET_TO_NULL) {
        target = target.set("user_id", entity.getUserId(), UUID.class);
      }
    }
    if (!lenient || hasProperty(target, "deleted_at")) {
      if (entity.getDeletedAt() != null || nullSavingStrategy == NullSavingStrategy.SET_TO_NULL) {
        target = target.set("deleted_at", entity.getDeletedAt(), Instant.class);
      }
    }
    if (!lenient || hasProperty(target, "type")) {
      if (entity.getType() != null || nullSavingStrategy == NullSavingStrategy.SET_TO_NULL) {
        target = target.set("type", entity.getType(), String.class);
      }
    }
    if (!lenient || hasProperty(target, "content")) {
      if (entity.getContent() != null || nullSavingStrategy == NullSavingStrategy.SET_TO_NULL) {
        target = target.set("content", entity.getContent(), String.class);
      }
    }
    return target;
  }

  @Override
  public Signal get(GettableByName source, boolean lenient) {
    Signal returnValue = new Signal();
    if (!lenient || hasProperty(source, "chat_id")) {
      UUID propertyValue = source.get("chat_id", UUID.class);
      returnValue.setChatId(propertyValue);
    }
    if (!lenient || hasProperty(source, "created_at")) {
      Instant propertyValue1 = source.get("created_at", Instant.class);
      returnValue.setCreatedAt(propertyValue1);
    }
    if (!lenient || hasProperty(source, "user_id")) {
      UUID propertyValue2 = source.get("user_id", UUID.class);
      returnValue.setUserId(propertyValue2);
    }
    if (!lenient || hasProperty(source, "deleted_at")) {
      Instant propertyValue3 = source.get("deleted_at", Instant.class);
      returnValue.setDeletedAt(propertyValue3);
    }
    if (!lenient || hasProperty(source, "type")) {
      String propertyValue4 = source.get("type", String.class);
      returnValue.setType(propertyValue4);
    }
    if (!lenient || hasProperty(source, "content")) {
      String propertyValue5 = source.get("content", String.class);
      returnValue.setContent(propertyValue5);
    }
    return returnValue;
  }

  @Override
  public RegularInsert insert() {
    throwIfKeyspaceMissing();
    InsertInto insertInto = (keyspaceId == null)
        ? QueryBuilder.insertInto(tableId)
        : QueryBuilder.insertInto(keyspaceId, tableId);
    return insertInto
        .value("chat_id", QueryBuilder.bindMarker("chat_id"))
        .value("created_at", QueryBuilder.bindMarker("created_at"))
        .value("user_id", QueryBuilder.bindMarker("user_id"))
        .value("deleted_at", QueryBuilder.bindMarker("deleted_at"))
        .value("type", QueryBuilder.bindMarker("type"))
        .value("content", QueryBuilder.bindMarker("content"));
  }

  public Select selectByPrimaryKeyParts(int parameterCount) {
    Select select = selectStart();
    for (int i = 0; i < parameterCount && i < primaryKeys.size(); i++) {
      String columnName = primaryKeys.get(i);
      select = select.whereColumn(columnName).isEqualTo(QueryBuilder.bindMarker(columnName));
    }
    return select;
  }

  @Override
  public Select selectByPrimaryKey() {
    return selectByPrimaryKeyParts(primaryKeys.size());
  }

  @Override
  public Select selectStart() {
    throwIfKeyspaceMissing();
    SelectFrom selectFrom = (keyspaceId == null)
        ? QueryBuilder.selectFrom(tableId)
        : QueryBuilder.selectFrom(keyspaceId, tableId);
    return selectFrom
        .column("chat_id")
        .column("created_at")
        .column("user_id")
        .column("deleted_at")
        .column("type")
        .column("content");
  }

  public DeleteSelection deleteStart() {
    throwIfKeyspaceMissing();
    return (keyspaceId == null)
        ? QueryBuilder.deleteFrom(tableId)
        : QueryBuilder.deleteFrom(keyspaceId, tableId);
  }

  public Delete deleteByPrimaryKeyParts(int parameterCount) {
    if (parameterCount <= 0) {
      throw new MapperException("parameterCount must be greater than 0");
    }
    DeleteSelection deleteSelection = deleteStart();
    String columnName = primaryKeys.get(0);
    Delete delete = deleteSelection.whereColumn(columnName).isEqualTo(QueryBuilder.bindMarker(columnName));
    for (int i = 1; i < parameterCount && i < primaryKeys.size(); i++) {
      columnName = primaryKeys.get(i);
      delete = delete.whereColumn(columnName).isEqualTo(QueryBuilder.bindMarker(columnName));
    }
    return delete;
  }

  @Override
  public Delete deleteByPrimaryKey() {
    return deleteByPrimaryKeyParts(primaryKeys.size());
  }

  @Override
  public DefaultUpdate updateStart() {
    throwIfKeyspaceMissing();
    UpdateStart update = (keyspaceId == null)
        ? QueryBuilder.update(tableId)
        : QueryBuilder.update(keyspaceId, tableId);
    return ((DefaultUpdate)update
        .setColumn("user_id", QueryBuilder.bindMarker("user_id"))
        .setColumn("deleted_at", QueryBuilder.bindMarker("deleted_at"))
        .setColumn("type", QueryBuilder.bindMarker("type"))
        .setColumn("content", QueryBuilder.bindMarker("content")));
  }

  @Override
  public DefaultUpdate updateByPrimaryKey() {
    return ((DefaultUpdate)updateStart()
        .where(Relation.column("chat_id").isEqualTo(QueryBuilder.bindMarker("chat_id")))
        .where(Relation.column("created_at").isEqualTo(QueryBuilder.bindMarker("created_at"))));
  }

  @Override
  public void validateEntityFields() {
    CqlIdentifier keyspaceId = this.keyspaceId != null ? this.keyspaceId : context.getSession().getKeyspace().orElse(null);
    String entityClassName = "org.tyniest.chat.entity.Signal";
    if (keyspaceId == null) {
      LOG.warn("[{}] Unable to validate table: {} for the entity class: {} because the keyspace is unknown (the entity does not declare a default keyspace, and neither the session nor the DAO were created with a keyspace). The DAO will only work if it uses fully-qualified queries with @Query or @QueryProvider.",
          context.getSession().getName(),
          tableId,
          entityClassName);
      return;
    }
    if(!keyspaceNamePresent(context.getSession().getMetadata().getKeyspaces(), keyspaceId)) {
      LOG.warn("[{}] Unable to validate table: {} for the entity class: {} because the session metadata has no information about the keyspace: {}.",
          context.getSession().getName(),
          tableId,
          entityClassName,
          keyspaceId);
      return;
    }
    Optional<KeyspaceMetadata> keyspace = context.getSession().getMetadata().getKeyspace(keyspaceId);
    List<CqlIdentifier> expectedCqlNames = new ArrayList<>();
    expectedCqlNames.add(CqlIdentifier.fromCql("chat_id"));
    expectedCqlNames.add(CqlIdentifier.fromCql("created_at"));
    expectedCqlNames.add(CqlIdentifier.fromCql("user_id"));
    expectedCqlNames.add(CqlIdentifier.fromCql("deleted_at"));
    expectedCqlNames.add(CqlIdentifier.fromCql("type"));
    expectedCqlNames.add(CqlIdentifier.fromCql("content"));
    Optional<TableMetadata> tableMetadata = keyspace.flatMap(v -> v.getTable(tableId));
    Optional<UserDefinedType> userDefinedType = keyspace.flatMap(v -> v.getUserDefinedType(tableId));
    if (tableMetadata.isPresent()) {
      // validation of missing Clustering Columns
      List<CqlIdentifier> expectedCqlClusteringColumns = new ArrayList<>();
      expectedCqlClusteringColumns.add(CqlIdentifier.fromCql("created_at"));
      List<CqlIdentifier> missingTableClusteringColumnNames = findMissingColumns(expectedCqlClusteringColumns, tableMetadata.get().getClusteringColumns().keySet());
      if (!missingTableClusteringColumnNames.isEmpty()) {
        throw new IllegalArgumentException(String.format("The CQL ks.table: %s.%s has missing Clustering columns: %s that are defined in the entity class: %s", keyspaceId, tableId, missingTableClusteringColumnNames, entityClassName));
      }
      // validation of missing PKs
      List<CqlIdentifier> expectedCqlPKs = new ArrayList<>();
      expectedCqlPKs.add(CqlIdentifier.fromCql("chat_id"));
      List<CqlIdentifier> missingTablePksNames = findMissingColumns(expectedCqlPKs, tableMetadata.get().getPartitionKey());
      if (!missingTablePksNames.isEmpty()) {
        throw new IllegalArgumentException(String.format("The CQL ks.table: %s.%s has missing Primary Key columns: %s that are defined in the entity class: %s", keyspaceId, tableId, missingTablePksNames, entityClassName));
      }
      // validation of all columns
      List<CqlIdentifier> missingTableCqlNames = findMissingCqlIdentifiers(expectedCqlNames, tableMetadata.get().getColumns().keySet());
      if (!missingTableCqlNames.isEmpty()) {
        throw new IllegalArgumentException(String.format("The CQL ks.table: %s.%s has missing columns: %s that are defined in the entity class: %s", keyspaceId, tableId, missingTableCqlNames, entityClassName));
      }
      // validation of types
      Map<CqlIdentifier, GenericType<?>> expectedTypesPerColumn = new LinkedHashMap<>();
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("deleted_at"), GENERIC_TYPE);
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("type"), GENERIC_TYPE1);
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("chat_id"), GENERIC_TYPE2);
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("user_id"), GENERIC_TYPE2);
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("content"), GENERIC_TYPE1);
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("created_at"), GENERIC_TYPE);
      List<String> missingTableTypes = findTypeMismatches(expectedTypesPerColumn, tableMetadata.get().getColumns(), context.getSession().getContext().getCodecRegistry());
      throwMissingTableTypesIfNotEmpty(missingTableTypes, keyspaceId, tableId, entityClassName);
    }
    else if (userDefinedType.isPresent()) {
      // validation of UDT columns
      List<CqlIdentifier> columns = userDefinedType.get().getFieldNames();
      List<CqlIdentifier> missingTableCqlNames = findMissingCqlIdentifiers(expectedCqlNames, columns);
      if (!missingTableCqlNames.isEmpty()) {
        throw new IllegalArgumentException(String.format("The CQL ks.udt: %s.%s has missing columns: %s that are defined in the entity class: %s", keyspaceId, tableId, missingTableCqlNames, entityClassName));
      }
      // validation of UDT types
      Map<CqlIdentifier, GenericType<?>> expectedTypesPerColumn = new LinkedHashMap<>();
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("deleted_at"), GENERIC_TYPE);
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("type"), GENERIC_TYPE1);
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("chat_id"), GENERIC_TYPE2);
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("user_id"), GENERIC_TYPE2);
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("content"), GENERIC_TYPE1);
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("created_at"), GENERIC_TYPE);
      List<CqlIdentifier> expectedColumns = userDefinedType.get().getFieldNames();
      List<DataType> expectedTypes = userDefinedType.get().getFieldTypes();
      List<String> missingTableTypes = findTypeMismatches(expectedTypesPerColumn, expectedColumns, expectedTypes, context.getSession().getContext().getCodecRegistry());
      throwMissingUdtTypesIfNotEmpty(missingTableTypes, keyspaceId, tableId, entityClassName);
    }
    // warn if there is not keyspace.table for defined entity - it means that table is missing, or schema it out of date.
    else {
      LOG.warn("[{}] There is no ks.table or UDT: {}.{} for the entity class: {}, or metadata is out of date.",
          context.getSession().getName(),
          keyspaceId,
          tableId,
          entityClassName);
    }
  }
}
