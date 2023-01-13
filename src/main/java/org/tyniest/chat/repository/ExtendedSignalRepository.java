package org.tyniest.chat.repository;

import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.entity.Signal;

import com.datastax.oss.driver.api.core.PagingIterable;

import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class ExtendedSignalRepository {
    private final SignalRepository signalRepository;

    int limit = 20;

    public PagingIterable<Signal> findByChatId(final UUID chatId, final Optional<UUID> offset) {
        return offset
            .map(o -> signalRepository.findByChatIdAndOffset(chatId, o, limit))
            .orElseGet(() -> signalRepository.findByChatId(chatId, limit));
    }
}
