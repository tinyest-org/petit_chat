package org.tyniest.utils.notifier.local;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.utils.notifier.ChannelHandle;
import org.tyniest.utils.notifier.ExistingConflictingChannel;
import org.tyniest.utils.notifier.NotifierService;

import io.vertx.mutiny.core.eventbus.EventBus;

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
        final var current = this.handles.contains(topic);
        if (current) {
            throw new ExistingConflictingChannel();
        }
        this.handles.add(topic);
        return new LocalChannelHandle<>(topic, bus, clazz);
    }
}


