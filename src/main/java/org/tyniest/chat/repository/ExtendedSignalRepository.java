package org.tyniest.chat.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.entity.Signal;
import org.tyniest.chat.entity.UserByChat;

import com.datastax.oss.driver.api.core.PagingIterable;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class ExtendedSignalRepository {
    private final SignalRepository signalRepository;

    
    public PagingIterable<Signal> findByChatId(UUID chatId, Optional<UUID> offset) {
        return offset.map(o -> {
            log.info("using offset");
            return signalRepository.findByChatIdAndOffset(chatId, o);
        }).orElseGet(() -> {
            return signalRepository.findByChatId(chatId);
        });
    }
}
