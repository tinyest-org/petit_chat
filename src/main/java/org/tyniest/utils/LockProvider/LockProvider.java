package org.tyniest.utils.LockProvider;

import io.smallrye.mutiny.Uni;
import java.time.Duration;

/** Implements distributed locks for applications */
public interface LockProvider {
    Uni<Boolean> tryReactiveAcquire(String name, Duration timeout);

    boolean tryAcquire(String name, Duration timeout);

    boolean tryAcquire(String name);

    void release(String name);
}
