package org.tyniest.chat.entity;

import java.util.UUID;

import com.datastax.oss.driver.api.mapper.annotations.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Builder
@EqualsAndHashCode(of = "uuid")
public class Chat {
    // chat has messages
    // chat has users
    private UUID uuid;
}
