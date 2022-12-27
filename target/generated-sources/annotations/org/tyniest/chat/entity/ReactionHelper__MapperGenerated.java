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
public class ReactionHelper__MapperGenerated extends EntityHelperBase<Reaction> {
  private static final Logger LOG = LoggerFactory.getLogger(ReactionHelper__MapperGenerated.class);

  private static final GenericType<UUID> GENERIC_TYPE = new GenericType<UUID>(){};

  private static final GenericType<String> GENERIC_TYPE1 = new GenericType<String>(){};

  private final List<String> primaryKeys;

  public ReactionHelper__MapperGenerated(MapperContext context) {
    super(context, "reaction");
    LOG.debug("[{}] Entity Reaction will be mapped to {}{}",
        context.getSession().getName(),
        getKeyspaceId() == null ? "" : getKeyspaceId() + ".",
        getTableId());
    this.primaryKeys = ImmutableList.<String>builder()
        .add("signal_id")
        .add("user_id")
        .add("value")
        .build();
  }

  @Override
  public Class<Reaction> getEntityClass() {
    return Reaction.class;
  }

  @Override
  public <SettableT extends SettableByName<SettableT>> SettableT set(Reaction entity,
      SettableT target, NullSavingStrategy nullSavingStrategy, boolean lenient) {
    if (!lenient || hasProperty(target, "signal_id")) {
      if (entity.getSignalId() != null || nullSavingStrategy == NullSavingStrategy.SET_TO_NULL) {
        target = target.set("signal_id", entity.getSignalId(), UUID.class);
      }
    }
    if (!lenient || hasProperty(target, "user_id")) {
      if (entity.getUserId() != null || nullSavingStrategy == NullSavingStrategy.SET_TO_NULL) {
        target = target.set("user_id", entity.getUserId(), UUID.class);
      }
    }
    if (!lenient || hasProperty(target, "value")) {
      if (entity.getValue() != null || nullSavingStrategy == NullSavingStrategy.SET_TO_NULL) {
        target = target.set("value", entity.getValue(), String.class);
      }
    }
    if (!lenient || hasProperty(target, "created_at")) {
      if (entity.getCreatedAt() != null || nullSavingStrategy == NullSavingStrategy.SET_TO_NULL) {
        target = target.set("created_at", entity.getCreatedAt(), UUID.class);
      }
    }
    return target;
  }

  @Override
  public Reaction get(GettableByName source, boolean lenient) {
    Reaction returnValue = new Reaction();
    if (!lenient || hasProperty(source, "signal_id")) {
      UUID propertyValue = source.get("signal_id", UUID.class);
      returnValue.setSignalId(propertyValue);
    }
    if (!lenient || hasProperty(source, "user_id")) {
      UUID propertyValue1 = source.get("user_id", UUID.class);
      returnValue.setUserId(propertyValue1);
    }
    if (!lenient || hasProperty(source, "value")) {
      String propertyValue2 = source.get("value", String.class);
      returnValue.setValue(propertyValue2);
    }
    if (!lenient || hasProperty(source, "created_at")) {
      UUID propertyValue3 = source.get("created_at", UUID.class);
      returnValue.setCreatedAt(propertyValue3);
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
        .value("signal_id", QueryBuilder.bindMarker("signal_id"))
        .value("user_id", QueryBuilder.bindMarker("user_id"))
        .value("value", QueryBuilder.bindMarker("value"))
        .value("created_at", QueryBuilder.bindMarker("created_at"));
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
        .column("signal_id")
        .column("user_id")
        .column("value")
        .column("created_at");
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
        .setColumn("created_at", QueryBuilder.bindMarker("created_at")));
  }

  @Override
  public DefaultUpdate updateByPrimaryKey() {
    return ((DefaultUpdate)updateStart()
        .where(Relation.column("signal_id").isEqualTo(QueryBuilder.bindMarker("signal_id")))
        .where(Relation.column("user_id").isEqualTo(QueryBuilder.bindMarker("user_id")))
        .where(Relation.column("value").isEqualTo(QueryBuilder.bindMarker("value"))));
  }

  @Override
  public void validateEntityFields() {
    CqlIdentifier keyspaceId = this.keyspaceId != null ? this.keyspaceId : context.getSession().getKeyspace().orElse(null);
    String entityClassName = "org.tyniest.chat.entity.Reaction";
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
    expectedCqlNames.add(CqlIdentifier.fromCql("signal_id"));
    expectedCqlNames.add(CqlIdentifier.fromCql("user_id"));
    expectedCqlNames.add(CqlIdentifier.fromCql("value"));
    expectedCqlNames.add(CqlIdentifier.fromCql("created_at"));
    Optional<TableMetadata> tableMetadata = keyspace.flatMap(v -> v.getTable(tableId));
    Optional<UserDefinedType> userDefinedType = keyspace.flatMap(v -> v.getUserDefinedType(tableId));
    if (tableMetadata.isPresent()) {
      // validation of missing Clustering Columns
      List<CqlIdentifier> expectedCqlClusteringColumns = new ArrayList<>();
      expectedCqlClusteringColumns.add(CqlIdentifier.fromCql("user_id"));
      expectedCqlClusteringColumns.add(CqlIdentifier.fromCql("value"));
      List<CqlIdentifier> missingTableClusteringColumnNames = findMissingColumns(expectedCqlClusteringColumns, tableMetadata.get().getClusteringColumns().keySet());
      if (!missingTableClusteringColumnNames.isEmpty()) {
        throw new IllegalArgumentException(String.format("The CQL ks.table: %s.%s has missing Clustering columns: %s that are defined in the entity class: %s", keyspaceId, tableId, missingTableClusteringColumnNames, entityClassName));
      }
      // validation of missing PKs
      List<CqlIdentifier> expectedCqlPKs = new ArrayList<>();
      expectedCqlPKs.add(CqlIdentifier.fromCql("signal_id"));
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
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("signal_id"), GENERIC_TYPE);
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("value"), GENERIC_TYPE1);
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("user_id"), GENERIC_TYPE);
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
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("signal_id"), GENERIC_TYPE);
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("value"), GENERIC_TYPE1);
      expectedTypesPerColumn.put(CqlIdentifier.fromCql("user_id"), GENERIC_TYPE);
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