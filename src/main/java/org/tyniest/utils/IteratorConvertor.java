package org.tyniest.utils;

import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IteratorConvertor {
    public static <T> Stream<T> iterableToStream(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T> Stream<T> spliteratorToStream(final Spliterator<T> iterable) {
        return StreamSupport.stream(iterable, false);
    }
}
