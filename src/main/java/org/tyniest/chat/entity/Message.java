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
public class Message {
    // messge is bound to user
    // and content
    // and Chat
    @PartitionKey
    private UUID id;

    private UUID chatId; // should be foundable by chatId
    private UUID userId; // should be foundable by userId

    @Builder.Default
    private Instant createdAt = Instant.now();

    private Instant deletedAt;
    
    private String content;

    // should handle attachments
}
