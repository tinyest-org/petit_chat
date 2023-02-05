package org.tyniest.user.entity;

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
public class User {
    @PartitionKey
    private UUID id; // same as SSO

    private String name;

    private String profilePicture;
    // TODO: add current statusINSERT INTO user (id, name)INSERT INTO user (id, name)

}
