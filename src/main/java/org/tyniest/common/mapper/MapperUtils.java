package org.tyniest.common.mapper;

import java.time.Instant;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.tyniest.config.MapstructConfig;

import com.datastax.oss.driver.api.core.uuid.Uuids;

@Mapper(config = MapstructConfig.class, uses = {})
public interface MapperUtils {
    default Instant timeUUIDToInstant(final UUID timeUid) {
        final var millis = Uuids.unixTimestamp(timeUid);
        return Instant.ofEpochMilli(millis);
    }
}
