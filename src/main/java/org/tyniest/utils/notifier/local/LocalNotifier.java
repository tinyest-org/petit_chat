package org.tyniest.utils.notifier.local;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Singleton;

import org.tyniest.utils.notifier.ChannelHandle;
import org.tyniest.utils.notifier.ExistingConflictingChannel;
import org.tyniest.utils.notifier.NotifierService;

import io.vertx.mutiny.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class LocalNotifier implements NotifierService {
    private final EventBus bus;
    private final Set<String> handles = new HashSet<>();

    public LocalNotifier(final EventBus bus) {
        this.bus = bus;
    }

    @Override
    public <T> ChannelHandle<T> getHandle(final String topic, final Class<T> clazz)
            throws ExistingConflictingChannel {
        log.info("registering: {}", topic);
        if (this.handles.contains(topic)) {
            throw new ExistingConflictingChannel();
        }
        this.handles.add(topic);
        return new LocalChannelHandle<>(topic, bus, clazz);
    }
}


