package org.hankster.functional.optional;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Adapters for converting between Guava's Optional and Java 8's Optional
 */
public interface OptionalAdapters {

    /**
     * Convert from Guava's Optional to Java 8's Optional
     * @param guavaOptional an instance of Guava's Optional
     * @param <T> the type of the Optional value
     * @return an equivalent Java 8 Optional
     */
    @Nonnull
    static <T> Optional<T> fromGuava(@Nonnull com.google.common.base.Optional<T> guavaOptional) {
        return Optional.ofNullable(guavaOptional.orNull());
    }

    /**
     * Convert from Java 8's Optional to Guava's Optional
     * @param optional an instance of Java 8's Optional
     * @param <T> the type of the Optional value
     * @return an equivalent Guava Optional
     */
    @Nonnull
    static <T> com.google.common.base.Optional<T> toGuava(@Nonnull Optional<T> optional) {
        return com.google.common.base.Optional.fromNullable(optional.orElse(null));
    }

}
