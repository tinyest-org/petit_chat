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
    
    
    @NotBlank
    @ClusteringColumn(1)
    private Integer type; // TODO: Enum
    
    @ClusteringColumn(2)
    private UUID createdAt; // timeuuid

    private Instant deletedAt;
    
    private UUID userId;

    
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

    public static final int TEXT_TYPE = 0; 
    public static final int FILE_TYPE = 1; 
    public static final int CALL_TYPE = 2; 
    public static final int ARRIVAL_TYPE = 3;
    public static final int LEFT_TYPE = 4;
    public static final int IMAGE_TYPE = 5;

    public Signal setArrivals() {
        this.type = ARRIVAL_TYPE;
        return this;
    }
 
    public Signal setLefts() {
        this.type = LEFT_TYPE;
        return this;
    }
    // TODO: add length -> useless for text but can be used for files to tell the size
}
