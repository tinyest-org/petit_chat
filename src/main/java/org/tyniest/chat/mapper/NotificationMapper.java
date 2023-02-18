package org.tyniest.chat.mapper;

import org.mapstruct.Mapper;
import org.tyniest.chat.dto.notification.AddedNotificationReactionDto;
import org.tyniest.chat.dto.notification.RemovedNotificationReactionDto;
import org.tyniest.chat.entity.Reaction;
import org.tyniest.config.MapstructConfig;

@Mapper(config = MapstructConfig.class)
public interface NotificationMapper {
    AddedNotificationReactionDto addedReactionDto(Reaction reaction);
    RemovedNotificationReactionDto removedReactionDto(Reaction reaction);
}
