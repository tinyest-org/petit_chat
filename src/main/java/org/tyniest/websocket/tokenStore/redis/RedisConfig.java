package org.tyniest.websocket.tokenStore.redis;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder
@Getter
@Accessors(fluent = true)
public class RedisConfig {
    private String userId;
    private String token;
    private String state;
    private String instanceCount;
}
