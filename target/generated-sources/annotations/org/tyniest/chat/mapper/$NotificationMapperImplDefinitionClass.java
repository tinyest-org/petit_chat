package org.tyniest.chat.mapper;

import javax.annotation.processing.Generated;
import javax.enterprise.context.ApplicationScoped;
import org.tyniest.chat.dto.notification.AddedNotificationReactionDto;
import org.tyniest.chat.dto.notification.RemovedNotificationReactionDto;
import org.tyniest.chat.entity.Reaction;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-18T17:20:01+0100",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.12 (GraalVM Community)"
)
@ApplicationScoped
public class $NotificationMapperImplDefinitionClass implements NotificationMapper {

    @Override
    public AddedNotificationReactionDto addedReactionDto(Reaction reaction) {
        if ( reaction == null ) {
            return null;
        }

        AddedNotificationReactionDto addedNotificationReactionDto = new AddedNotificationReactionDto();

        addedNotificationReactionDto.setUserId( reaction.getUserId() );
        addedNotificationReactionDto.setValue( reaction.getValue() );
        addedNotificationReactionDto.setSignalId( reaction.getSignalId() );

        return addedNotificationReactionDto;
    }

    @Override
    public RemovedNotificationReactionDto removedReactionDto(Reaction reaction) {
        if ( reaction == null ) {
            return null;
        }

        RemovedNotificationReactionDto removedNotificationReactionDto = new RemovedNotificationReactionDto();

        removedNotificationReactionDto.setUserId( reaction.getUserId() );
        removedNotificationReactionDto.setValue( reaction.getValue() );
        removedNotificationReactionDto.setSignalId( reaction.getSignalId() );

        return removedNotificationReactionDto;
    }
}
