package org.tyniest.files.entity;

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
@EqualsAndHashCode(of = "fid")
public class File {
    @PartitionKey
    private String fid;

    /**
     * Volume where the file is stored in seaweed cluster
     */
    private Integer volumeId;

    // in order to dedupe data
    private byte[] hash;
}
