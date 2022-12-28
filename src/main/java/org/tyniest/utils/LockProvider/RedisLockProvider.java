package org.tyniest.utils.LockProvider;

import java.time.Duration;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.smallrye.mutiny.Uni;

/** Implements lock based on Redis database */
public class RedisLockProvider implements LockProvider {

    private final String lockToken;

    private final ReactiveValueCommands <String, String> commands;
    private final ReactiveKeyCommands<String> key;

    public RedisLockProvider(final String lockToken, final ReactiveRedisDataSource ds) {
        this.lockToken = lockToken;
        this.commands = ds.value(String.class);
        this.key = ds.key();
    }

    @Override
    public Uni<Boolean> tryReactiveAcquire(final String name, final Duration timeout) {
        final var args =
                new SetArgs()
                        .nx() // prevent from being set twice
                        .ex(timeout); // auto deletes key if timeout
        final var res =
                this.commands
                        .set(makeKey(name), "", args) // empty string value as we need nothing
                        .onItem()
                        .transform(e -> true) // e should be null
                        .onFailure()
                        .recoverWithItem(false); // returns false if the key already exists
        return res;
    }

    /** */
    protected String makeKey(final String name) {
        return lockToken + name;
    }

    /**
     * @param name name of the lock, should not be empty
     */
    @Override
    public void release(final String name) {
        this.key.del(makeKey(name)).replaceWithVoid().await().indefinitely();
    }

    @Override
    public boolean tryAcquire(String name, Duration timeout) {
        return tryReactiveAcquire(name, timeout).await().indefinitely().booleanValue();
    }

    @Override
    public boolean tryAcquire(String name) {
        return this.tryAcquire(name, Duration.ofMillis(0));
    }
}
