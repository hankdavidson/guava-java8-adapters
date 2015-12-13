package org.hankster.functional.streams;

import com.google.common.collect.FluentIterable;

import javax.annotation.Nonnull;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Adapters for converting FluentIterables to and from Streams
 */
public interface StreamAdapters {
    @Nonnull
    static <T> Stream<T> fromFluentIterable(@Nonnull FluentIterable<T> fi) {
        return Helpers.fromIterable(fi);
    }

    @Nonnull
    static <T> FluentIterable<T> toFluentIterable(@Nonnull Stream<T> stream) {
        return FluentIterable.from(Helpers.toIterable(stream));
    }

    interface Helpers {
        @Nonnull
        static <T> Stream<T> fromIterable(@Nonnull Iterable<T> iterable) {
            return StreamSupport.stream(iterable.spliterator(), false);
        }

        @Nonnull
        static <T> Iterable<T> toIterable(@Nonnull Stream<T> stream) {
            return stream::iterator;
        }
    }
}
