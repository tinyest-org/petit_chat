package org.tyniest.chat.entity;

import java.time.Instant;
import java.util.UUID;

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
@EqualsAndHashCode(of = "id")
public class Chat {
    // chat has messages
    // chat has users
    @PartitionKey
    private UUID id;

    private UUID createdAt;
    private UUID lastUpdatedAt;
    private String name;

    // private List<UUID> userIds;
}
