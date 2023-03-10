package org.tyniest.chat.entity;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "signalId")
public class Reaction {
    
    @PartitionKey
    private UUID signalId; // thats the createdAt column of the signal
    
    @NotBlank
    @ClusteringColumn(2) // we can have multiple reactions to the signal
    private String value; // name of reaction

    @ClusteringColumn(1) // we can have multiple reactions to the signal
    private UUID userId;
    
    // TODO: re add this as a clustering key as, it will be required for paging
    // @ClusteringColumn(3) // we can have multiple reactions to the signal
    @NotNull
    private UUID createdAt;
}
