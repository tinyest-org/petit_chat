package org.tyniest.utils.LockProvider;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

/**
 * Defines a concurrency lock for a bean.
 *
 * <p>The container provides a built-in interceptor for this interceptor binding. Each interceptor
 * instance associated with a contextual instance of an intercepted bean holds a {@link
 * ReadWriteLock} instance with non-fair ordering policy.
 */
@InterceptorBinding
@Inherited
@Target(value = {TYPE, METHOD})
@Retention(value = RUNTIME)
public @interface DistributedLock {

    /**
     * @return the type of the lock
     */
    @Nonbinding
    String value();

    /**
     * If it's not possible to acquire the lock in the given time a {@link LockException} is thrown.
     *
     * @see java.util.concurrent.locks.Lock#tryLock(long, TimeUnit)
     * @return the wait time
     */
    @Nonbinding
    int time();

    /**
     * @return the wait time unit
     */
    @Nonbinding
    TimeUnit unit();
}
