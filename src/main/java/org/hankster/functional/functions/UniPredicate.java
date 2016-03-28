package org.hankster.functional.functions;


@FunctionalInterface
public interface UniPredicate<T> extends java.util.function.Predicate<T>, com.google.common.base.Predicate<T> {

    /**
     * Since Java 8's Predicate has a function called "test" and Guava's Predicate has a function called "apply",
     * an interface that extends both cannot be annotated with @FunctionalInterface.  However, by supplying a default
     * function for one of them, we get around the limitation, allowing UniPredicate to be used directly by both
     * Java 8 and Guava apis
     *
     * @param t the value to test
     * @return the result of the function call
     */
    default boolean test(T t) {
        return apply(t);
    }

    /**
     * Java 8's Predicate implements the negate() operation as a default method.  However, calling it on a Unipredicate
     * returns only Java 8's Predicate type.  This allows it to be called but retain its identity as a Unipredicate.
     *
     * @return a UniPredicate that negates the logic of this instance
     */
    default UniPredicate<T> negate() {
        return java.util.function.Predicate.super.negate()::test;
    }


}
