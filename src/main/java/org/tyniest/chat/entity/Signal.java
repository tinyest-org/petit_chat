package org.tyniest.chat.entity;

import java.time.Instant;
import java.util.UUID;

import com.datastax.oss.driver.api.core.uuid.Uuids;
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
@EqualsAndHashCode(of = {"chatId", "createdAt"})
public class Signal {
    @PartitionKey
    private UUID chatId; // should be foundable by chatId
    
    @ClusteringColumn
    private UUID createdAt; // timeuuid

    private Instant deletedAt;
    
    private UUID userId;

    private String type; // TOOD: Enum
    private String content;

    public Instant getCreatedAtDate() {
        final var seconds = Uuids.unixTimestamp(this.createdAt);
        final var millis =  seconds / 1000;
        return Instant.ofEpochSecond(millis, 0);
    }

    // TODO: handle attachments
}
