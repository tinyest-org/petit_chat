package org.tyniest.chat.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import javax.enterprise.context.ApplicationScoped;
import org.tyniest.chat.dto.SignalDto;
import org.tyniest.chat.entity.Signal;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-26T14:26:00+0100",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.12 (GraalVM Community)"
)
@ApplicationScoped
public class $SignalMapperImplDefinitionClass implements SignalMapper {

    @Override
    public SignalDto asDto(Signal signal) {
        if ( signal == null ) {
            return null;
        }

        SignalDto signalDto = new SignalDto();

        signalDto.setUuid( signal.getCreatedAt() );
        signalDto.setContent( signal.getContent() );
        signalDto.setType( signal.getType() );
        signalDto.setCreatedAt( timeUUIDToInstant( signal.getCreatedAt() ) );

        return signalDto;
    }

    @Override
    public List<SignalDto> asDto(List<Signal> signal) {
        if ( signal == null ) {
            return null;
        }

        List<SignalDto> list = new ArrayList<SignalDto>( signal.size() );
        for ( Signal signal1 : signal ) {
            list.add( asDto( signal1 ) );
        }

        return list;
    }
}
