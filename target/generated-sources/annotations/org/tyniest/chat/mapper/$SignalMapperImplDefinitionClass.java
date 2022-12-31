package org.tyniest.chat.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.processing.Generated;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.tyniest.chat.dto.BasicSignalDto;
import org.tyniest.chat.dto.ReactionDto;
import org.tyniest.chat.dto.SignalDto;
import org.tyniest.chat.entity.Reaction;
import org.tyniest.chat.entity.Signal;
import org.tyniest.common.mapper.MapperUtils;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-31T14:28:21+0100",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.12 (GraalVM Community)"
)
@ApplicationScoped
public class $SignalMapperImplDefinitionClass implements SignalMapper {

    private MapperUtils mapperUtils;

    public $SignalMapperImplDefinitionClass() {
    }

    @Inject
    public $SignalMapperImplDefinitionClass(MapperUtils mapperUtils) {

        this.mapperUtils = mapperUtils;
    }

    @Override
    public SignalDto asDto(Signal signal, List<Reaction> reactions) {
        if ( signal == null && reactions == null ) {
            return null;
        }

        SignalDto signalDto = new SignalDto();

        if ( signal != null ) {
            signalDto.setUuid( signal.getCreatedAt() );
            signalDto.setContent( signal.getContent() );
            signalDto.setType( signal.getType() );
            signalDto.setCreatedAt( mapperUtils.timeUUIDToInstant( signal.getCreatedAt() ) );
        }
        signalDto.setReactions( reactionListToReactionDtoList( reactions ) );

        return signalDto;
    }

    @Override
    public List<BasicSignalDto> asBasicDto(Stream<Signal> signals) {
        if ( signals == null ) {
            return null;
        }

        return signals.map( signal -> asBasicDto( signal ) )
        .collect( Collectors.toCollection( ArrayList<BasicSignalDto>::new ) );
    }

    @Override
    public BasicSignalDto asBasicDto(Signal signal) {
        if ( signal == null ) {
            return null;
        }

        BasicSignalDto basicSignalDto = new BasicSignalDto();

        basicSignalDto.setUuid( signal.getCreatedAt() );
        basicSignalDto.setContent( signal.getContent() );
        basicSignalDto.setType( signal.getType() );
        basicSignalDto.setCreatedAt( mapperUtils.timeUUIDToInstant( signal.getCreatedAt() ) );

        return basicSignalDto;
    }

    protected ReactionDto reactionToReactionDto(Reaction reaction) {
        if ( reaction == null ) {
            return null;
        }

        ReactionDto reactionDto = new ReactionDto();

        reactionDto.setUserId( reaction.getUserId() );
        reactionDto.setValue( reaction.getValue() );

        return reactionDto;
    }

    protected List<ReactionDto> reactionListToReactionDtoList(List<Reaction> list) {
        if ( list == null ) {
            return null;
        }

        List<ReactionDto> list1 = new ArrayList<ReactionDto>( list.size() );
        for ( Reaction reaction : list ) {
            list1.add( reactionToReactionDto( reaction ) );
        }

        return list1;
    }
}
