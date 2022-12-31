package org.tyniest.chat.entity;

import java.util.UUID;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"userId", "chatId"})
public class ChatUserCursor {
    @PartitionKey
    private UUID chatId;

    @ClusteringColumn(1)
    private UUID userId;

    @ClusteringColumn(2)
    private UUID lastSignalRead; // timeuuid

    private UUID updatedAt; // timeuuid
}
