package org.tyniest.chat.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.entity.Signal;

import com.datastax.oss.driver.api.core.PagingIterable;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class FullSignalRepository {
    private final SignalRepository signalRepository;

    
    public void save(Signal message) {
        signalRepository.save(message);
    }

    
    public Optional<Signal> findByChatIdAndCreatedAt(UUID chatId, UUID createdAt) {
        return signalRepository.findByChatIdAndCreatedAt(chatId, createdAt);
    }

    
    public PagingIterable<Signal> findByChatId(UUID chatId, Optional<UUID> offset) {
        return offset.map(o -> {
            log.info("using offset");
            return signalRepository.findByChatIdAndOffset(chatId, o);
        }).orElseGet(() -> {
            return signalRepository.findByChatId(chatId);
        });
        
    }

    public PagingIterable<Signal> findAllByIds(UUID chatId, List<UUID> ids) {
        return signalRepository.findAllByIds(chatId, ids);
    }
}
