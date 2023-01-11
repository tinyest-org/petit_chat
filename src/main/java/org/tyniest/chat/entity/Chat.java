package org.tyniest.chat.entity;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    
    @PartitionKey
    private UUID id;

    @NotNull
    private UUID createdAt;
    
    @NotNull
    private UUID lastUpdatedAt;
    
    @NotBlank
    private String name;

    public static String INDEX_NAME = "chat_messages";

    public String getIndexName() {
        return INDEX_NAME+this.id;
    }


    public static String getIndexName(final UUID id) {
        return INDEX_NAME+id;
    }

}
