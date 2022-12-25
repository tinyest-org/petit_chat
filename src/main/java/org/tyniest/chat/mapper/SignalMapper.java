package org.tyniest.chat.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.tyniest.chat.dto.SignalDto;
import org.tyniest.chat.entity.Signal;
import org.tyniest.config.MapstructConfig;

@Mapper(config = MapstructConfig.class)
public interface SignalMapper {
    SignalDto asDto(Signal signal);

    List<SignalDto> asDto(List<Signal> signal);
}
