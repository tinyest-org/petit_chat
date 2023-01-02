package org.tyniest.chat.repository;

import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.entity.Signal;
import org.tyniest.utils.UniHelper;

import com.datastax.oss.driver.api.core.PagingIterable;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class ExtendedSignalRepository {
    private final SignalRepository signalRepository;

    int limit = 20;

    public PagingIterable<Signal> findByChatId(final UUID chatId, final Optional<UUID> offset) {
        return offset
            .map(o -> signalRepository.findByChatIdAndOffset(chatId, o))
            .orElseGet(() -> signalRepository.findByChatId(chatId));
    }
}
