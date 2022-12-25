package org.tyniest.chat.entity;

import java.time.Instant;
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
@EqualsAndHashCode(of = {"chatId", "createdAt"})
public class Signal {
    @PartitionKey
    private UUID chatId; // should be foundable by chatId
    
    @Builder.Default
    // @PartitionKey(2)
    @ClusteringColumn
    private Instant createdAt = Instant.now();
    private UUID userId;

    private Instant deletedAt;
    
    private String type;
    private String content;

    // should handle attachments
}
