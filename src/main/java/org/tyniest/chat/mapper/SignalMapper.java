package org.tyniest.chat.mapper;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.tyniest.chat.dto.SignalDto;
import org.tyniest.chat.entity.Reaction;
import org.tyniest.chat.entity.Signal;
import org.tyniest.config.MapstructConfig;

import com.datastax.oss.driver.api.core.uuid.Uuids;

@Mapper(config = MapstructConfig.class)
public interface SignalMapper {
    @Named("dto")
    @Mapping(source = "signal.createdAt", target = "uuid")
    SignalDto asDto(Signal signal, List<Reaction> reactions);

    default Instant timeUUIDToInstant(final UUID timeUid) {
        final var millis = Uuids.unixTimestamp(timeUid);
        return Instant.ofEpochMilli(millis);
    }
}
