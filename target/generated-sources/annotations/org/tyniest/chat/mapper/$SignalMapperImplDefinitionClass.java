package org.tyniest.chat.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import javax.enterprise.context.ApplicationScoped;
import org.tyniest.chat.dto.SignalDto;
import org.tyniest.chat.entity.Signal;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-25T18:48:38+0100",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 1.4.200.v20221012-0724, environment: Java 17.0.5 (Eclipse Adoptium)"
)
@ApplicationScoped
public class $SignalMapperImplDefinitionClass implements SignalMapper {

    @Override
    public SignalDto asDto(Signal signal) {
        if ( signal == null ) {
            return null;
        }

        SignalDto signalDto = new SignalDto();

        signalDto.setContent( signal.getContent() );
        signalDto.setType( signal.getType() );

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
