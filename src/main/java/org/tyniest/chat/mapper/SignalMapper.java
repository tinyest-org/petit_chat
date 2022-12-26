package org.tyniest.chat.mapper;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.tyniest.chat.dto.SignalDto;
import org.tyniest.chat.entity.Signal;
import org.tyniest.config.MapstructConfig;

import com.datastax.oss.driver.api.core.uuid.Uuids;

@Mapper(config = MapstructConfig.class)
public interface SignalMapper {
    @Mapping(source = "createdAt", target = "uuid")
    SignalDto asDto(Signal signal);

    List<SignalDto> asDto(List<Signal> signal);

    default Instant timeUUIDToInstant(final UUID timeUid) {
        final var millis = Uuids.unixTimestamp(timeUid);
        return Instant.ofEpochMilli(millis);
    }
}
