package org.tyniest.utils.LockProvider;

import static javax.interceptor.Interceptor.Priority.PLATFORM_BEFORE;

import io.quarkus.arc.ArcInvocationContext;
import io.quarkus.arc.LockException;
import java.lang.annotation.Annotation;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@DistributedLock(value = "", unit = TimeUnit.MILLISECONDS, time = -1)
@Interceptor
@Priority(PLATFORM_BEFORE)
public class DistributedLockInterceptor {

    @Inject LockProvider lockProvider;

    @AroundInvoke
    Object handleLock(final InvocationContext ctx) throws Exception {
        final var l = getLock(ctx);
        return writeLock(l, ctx);
    }

    private Object writeLock(final DistributedLock lock, final InvocationContext ctx)
            throws Exception {
        final var hasLock =
                lockProvider.tryAcquire(
                        lock.value(), Duration.of(lock.time(), lock.unit().toChronoUnit()));
        try {
            if (hasLock) {
                return ctx.proceed();
            }
        } finally {
            lockProvider.release(lock.value());
        }
        throw new LockException("already running");
    }

    @SuppressWarnings("unchecked")
    DistributedLock getLock(final InvocationContext ctx) {
        Set<Annotation> bindings =
                (Set<Annotation>)
                        ctx.getContextData().get(ArcInvocationContext.KEY_INTERCEPTOR_BINDINGS);
        for (Annotation annotation : bindings) {
            if (annotation.annotationType().equals(DistributedLock.class)) {
                return (DistributedLock) annotation;
            }
        }
        // This should never happen
        throw new LockException(
                "@DistributedLock binding not found on business method " + ctx.getMethod());
    }
}
