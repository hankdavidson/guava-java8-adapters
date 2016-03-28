package org.hankster.functional.functions;

/**
 * This serves to point out that Guava's and Java 8's Function have compatible signatures and so it is possible
 * to create a functional interface that extends both functional interfaces, making it usable in both Guava and
 * Java 8 apis.  The Supplier functional interface from both APIs is similarly compatible. Unfortunately, the same
 * is not true for the respective apis' Predicate interface, since the function names do not match.
 * <p>
 * The fact that Guava's Function interface declares a second function, equals(), does not disqualify Guava's
 * Function interface as a Functional Interface, since methods implemented by Object are excluded from consideration
 * as functional interface SAMs.  See JLS, §9.8
 * @param <T> type of input to the Function
 * @param <R> return value of the Function.
 */
@FunctionalInterface
public interface UniFunction<T, R> extends java.util.function.Function<T, R>, com.google.common.base.Function<T, R> {
}
