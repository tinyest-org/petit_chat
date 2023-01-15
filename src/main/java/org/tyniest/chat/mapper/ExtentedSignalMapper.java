package org.tyniest.chat.mapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.dto.BasicSignalDto;
import org.tyniest.chat.dto.SignalDto;
import org.tyniest.chat.entity.Reaction;
import org.tyniest.chat.entity.Signal;
import org.tyniest.utils.seaweed.SeaweedClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class ExtentedSignalMapper {
    private final SignalMapper baseMapper;
    private final SeaweedClient client;

    public BasicSignalDto asBasicDto(final Signal signal) {
        return baseMapper.asBasicDto(signal);
    }

    public List<BasicSignalDto> asBasicDto(final Stream<Signal> signals) {
        return baseMapper.asBasicDto(signals);
    }

    public SignalDto asDto(final Signal signal, final List<Reaction> reactions) {
        switch(signal.getType()) {
            case Signal.TEXT_TYPE:
            return asTextDto(signal, reactions);
            case Signal.FILE_TYPE:
            return asFileDto(signal, reactions);
            default: return asTextDto(signal, reactions);
        }
    }

    public SignalDto asDto(final Signal signal) {
        return asDto(signal, List.of());
    }

    public List<SignalDto> asDto(final Stream<Signal> signal) {
        return signal.map(this::asDto).collect(Collectors.toList());
    }

    public List<SignalDto> asDto(final List<Signal> signal) {
        return asDto(signal.stream());
    }

    protected SignalDto asTextDto(final Signal signal, List<Reaction> reactions) {
        return baseMapper.asDto(signal, reactions);
    }

    protected SignalDto asFileDto(final Signal signal, List<Reaction> reactions) {
        final var res = baseMapper.asDto(signal, reactions);
        try {
            res.setContent(client.renderUrl(res.getContent()).await().indefinitely());
        } catch (Exception e) {
            log.error("failed to render url");
        }
        
        return res;
    }
}
