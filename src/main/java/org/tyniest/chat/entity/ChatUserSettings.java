package org.tyniest.chat.entity;

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
@EqualsAndHashCode(of = {"chatId", "userId"})
public class ChatUserSettings {
    @PartitionKey(1)
    private UUID chatId;
    
    @PartitionKey(2)
    private UUID userId;


    // TODO: add settings here
}
