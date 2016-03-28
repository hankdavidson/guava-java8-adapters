package org.hankster.functional.functions;

import javax.annotation.Nonnull;

/**
 * Adapters for conversion between Guava's and Java 8's functional interfaces. It is so easy to convert between them
 * using method references, that this serves more as a reference than a serious API
 */
public interface FunctionAdapters {

    /**
     * Convert from a Guava Predicate to a Java 8 Predicate.
     * @param gPred a Guava Predicate
     * @param <T> type of input to the Predicate
     * @return a Java 8 Predicate that wraps the given Guava Predicate
     */
    @Nonnull
    static <T> java.util.function.Predicate<T> fromGuavaPred(@Nonnull com.google.common.base.Predicate<T> gPred) {
        return gPred::apply;
    }

    /**
     * Convert from a Java 8 Predicate to a Guava Predicate
     * @param pred a Java 8 Predicate
     * @param <T> type of input to the Predicate
     * @return a Guava Predicate that wraps teh given Java 8 Predicate
     */
    @Nonnull
    static <T> com.google.common.base.Predicate<T> toGuavaPred(java.util.function.Predicate<T> pred) {
        return pred::test;
    }

    /**
     * Convert from a Guava Function to a Java 8 Function.
     * @param gFunc a Guava Function
     * @param <T> type of input to the Function
     * @param <R> type of return value from the Function
     * @return a Java 8 Function that wraps the given Guava Function
     */
    @Nonnull
    static <T, R> java.util.function.Function<T, R> fromGuavaFunc(@Nonnull com.google.common.base.Function<T, R> gFunc) {
        return gFunc::apply;
    }

    /**
     * Convert from a Java 8 Function to a Guava Function
     * @param func a Java 8 Function
     * @param <T> type of input to the Function
     * @param <R> type of return value from the Function
     * @return a Guava Function that wraps teh given Java 8 Function
     */
    @Nonnull
    static <T, R> com.google.common.base.Function<T, R> toGuavaFunc(java.util.function.Function<T, R> func) {
        return func::apply;
    }

    /**
     * Convert from a Guava Supplier to a Java 8 Supplier.
     * @param gSupplier a Guava Supplier
     * @param <R> type of return value from the Supplier
     * @return a Java 8 Supplier that wraps the given Guava Supplier
     */
    @Nonnull
    static <R> java.util.function.Supplier<R> fromGuavaSupplier(@Nonnull com.google.common.base.Supplier<R> gSupplier) {
        return gSupplier::get;
    }

    /**
     * Convert from a Java 8 Supplier to a Guava Supplier
     * @param supplier a Java 8 Supplier
     * @param <R> type of return value from the Supplier
     * @return a Guava Supplier that wraps teh given Java 8 Supplier
     */
    @Nonnull
    static <R> com.google.common.base.Supplier<R> toGuavaSupplier(java.util.function.Supplier<R> supplier) {
        return supplier::get;
    }

}
