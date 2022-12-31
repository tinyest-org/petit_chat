package org.tyniest.chat.entity;

import java.time.Instant;
import java.util.UUID;

import javax.validation.constraints.NotBlank;

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

    @NotBlank
    private String type; // TOOD: Enum
    @NotBlank
    private String content;

    // use collection to store the ids of people who read the message
    // TODO: collection of read -> List<Tuple<UserId, At>>
    // TODO: collection of delivered -> List<Tuple<UserId, At>> ?

    public Instant getCreatedAtDate() {
        final var seconds = Uuids.unixTimestamp(this.createdAt);
        final var millis =  seconds / 1000;
        return Instant.ofEpochSecond(millis, 0);
    }

    private static final String SIGNAL_TEXT_TYPE = "text";
    private static final String SIGNAL_ARRIVAL_TYPE = "move/join";
    private static final String SIGNAL_LEFT_TYPE = "move/leave";

    public void setArrivals() {
        this.type = SIGNAL_ARRIVAL_TYPE;
    }

    public void setLefts() {
        this.type = SIGNAL_LEFT_TYPE;
    }

    public static Signal ofText(
        final UUID chatId, final UUID createdAt, 
        final UUID userId, final String content
    ) {
        return Signal.builder()
            .chatId(chatId)
            .createdAt(createdAt)
            .userId(userId)
            .content(content)
            .type(SIGNAL_TEXT_TYPE)
            .build();
    }
    // TODO: handle attachments
}
