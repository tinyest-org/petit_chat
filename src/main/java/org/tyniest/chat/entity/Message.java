package org.tyniest.chat.entity;

import java.time.Instant;
import java.util.UUID;

import org.tyniest.user.entity.User;

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
@EqualsAndHashCode(of = "uuid")
public class Message {
    // messge is bound to user
    // and content
    // and Chat
    @PartitionKey
    private UUID uuid;

    private Chat chat;

    private Instant createdAt;

    private String content;

    private User user;
}
