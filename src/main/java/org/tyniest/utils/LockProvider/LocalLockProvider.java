package org.tyniest.utils.LockProvider;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;

import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class LocalLockProvider implements LockProvider {

    private final Map<String, LocalDateTime> locks = new ConcurrentHashMap<>();

    /**
     * @param timeout not handled for now, as should not be useful locally
     */
    @Override
    public boolean tryAcquire(String name, Duration timeout) {
        final var at = this.locks.get(name);
        if (at == null) {
            this.locks.put(name, LocalDateTime.now().plus(timeout));
            return true; // we got the lock
        }
        return false;
    }

    @Override
    public Uni<Boolean> tryReactiveAcquire(String name, Duration timeout) {
        return Uni.createFrom().item(tryAcquire(name, timeout));
    }

    @Override
    public void release(String name) {
        this.locks.remove(name);
    }

    @Override
    public boolean tryAcquire(String name) {
        return this.tryAcquire(name, Duration.ofMillis(0));
    }
}
