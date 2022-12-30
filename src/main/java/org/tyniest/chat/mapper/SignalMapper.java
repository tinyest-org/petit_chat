package org.tyniest.chat.mapper;

import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.tyniest.chat.dto.BasicSignalDto;
import org.tyniest.chat.dto.SignalDto;
import org.tyniest.chat.entity.Reaction;
import org.tyniest.chat.entity.Signal;
import org.tyniest.config.MapstructConfig;

@Mapper(config = MapstructConfig.class)
public interface SignalMapper {
    @Named("dto")
    @Mapping(source = "signal.createdAt", target = "uuid")
    SignalDto asDto(Signal signal, List<Reaction> reactions);

    @IterableMapping(qualifiedByName = "basicDto")
    List<BasicSignalDto> asBasicDto(Stream<Signal> signals);

    @Named("basicDto")
    @Mapping(source = "signal.createdAt", target = "uuid")
    BasicSignalDto asBasicDto(Signal signal);
}
