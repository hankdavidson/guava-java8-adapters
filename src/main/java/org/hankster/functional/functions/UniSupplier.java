package org.hankster.functional.functions;

/**
 * This serves to point out that Guava's and Java 8's Supplier have compatible signatures and so it is possible
 * to create a functional interface that extends both functional interfaces, making it usable in both Guava and
 * Java 8 apis.  The Function functional interface from both APIs is similarly compatible. Unfortunately, the same
 * is not true for the respective apis' Predicate interface, since the function names do not match.
 */
@FunctionalInterface
public interface UniSupplier<R> extends java.util.function.Supplier<R>, com.google.common.base.Supplier<R> {
}
