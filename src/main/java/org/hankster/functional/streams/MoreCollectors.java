package org.hankster.functional.streams;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import com.google.common.collect.ImmutableMap.Builder;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import java.util.stream.Collectors;

/**
 * Additional collectors for collecting to Maps not found in {@link Collectors}
 * Variations that take a finisher function are not given here, since finishers can always be applied using
 * Collectors.collectingAndThen().
 * <p>
 * Note on the use of interfaces for utility classes: With the advent of static methods for interfaces in Java 8,
 * interfaces become a compelling choice for utility classes, since there is no need to create a private constructor, etc.
 */
interface MoreCollectors {

    /**
     * Collector for collecting to a Guava BiMap (a map of unique values to other unique values that can be inverted)
     *
     * @param keyExtractor        A function that supplies the keys (which are the values of the inverted map)
     * @param reverseKeyExtractor A function that supplies the values (which are the keys of the inverted map)
     * @param <T>                 The upstream type
     * @param <K1>                The map key type
     * @param <K2>                The inverted map key type
     * @return a BiMap&lt;K1,K2&gt;
     */
    @Nonnull
    static <T, K1, K2> Collector<T, ?, BiMap<K1, K2>> toBiMap(@Nonnull Function<? super T, ? extends K1> keyExtractor,
                                                              @Nonnull Function<? super T, ? extends K2> reverseKeyExtractor) {
        return Collectors.toMap(keyExtractor, reverseKeyExtractor, Helpers::alwaysThrow, HashBiMap::create);
    }

    /**
     * Collector for collecting to a Guava Multiset, using HashMultiset as its implementation.  Use Helpers.toCollection
     * to select a different implementation of Multiset.
     * @param <T> The upstream type
     * @return a Multiset&lt;T&gt;
     */
    @Nonnull
    static <T> Collector<T, ?, Multiset<T>> toMultiset() {
        return Helpers.toCollection(HashMultiset::create, false);
    }

    /**
     * A collector for collecting to a Guava Multiset with the given initial number of distinct elements.  Use
     * Helpers.toCollectionWithCapacity to select a different implementation of Multiset.
     * @param initialDistinctElements the number of distinct elements to preallocate
     * @param <T> The upstream type
     * @return a Multiset&lt;T&gt;
     */
    @Nonnull
    static <T> Collector<T, ?, Multiset<T>> toMultiset(int initialDistinctElements) {
        return Helpers.toCollectionWithCapacity(HashMultiset::create, initialDistinctElements, false);
    }

    /**
     * Collector for collecting to a Guava Multiset, using ConcurrentHashMultiset as its implementation.
     * @param <T> The upstream type.  Using this collector will allow the Stream being collected to employ optimizations
     *           when dealing with parallel streams
     * @return a Multiset&lt;T&gt;
     */
    @Nonnull
    static <T> Collector<T, ?, Multiset<T>> toConcurrentMultiset() {
        return Collector.of(
                (Supplier<Multiset<T>>) ConcurrentHashMultiset::create,
                Collection::add,
                Helpers.mergeWith(Collection::addAll),
                Helpers.UNORDERED_ID_FINISH_CONCURRENT);
    }

    /**
     * Collector for collecting to a Guava Multiset, using LinkedHashMultiset as its implementation.  Because
     * LinkedHashMultiset preserves encounter order, this collector will signal the Stream being collected to avoid
     * optimizations that would fail to preserve the order of the stream elements. Note that this collector may not
     * perform as well as collectors that are free to violate encounter order.
     * @param <T> The upstream type
     * @return a Multiset&lt;T&gt;
     */
    @Nonnull
    static <T> Collector<T, ?, Multiset<T>> toLinkedMultiset() {
        return Helpers.toCollection(LinkedHashMultiset::create, true);
    }

    /**
     * Collector for collecting to a Guava Multiset with the given initial number of distinct elements, using
     * LinkedHashMultiset as its implementation.  Because LinkedHashMultiset preserves encounter order, this collector
     * will signal the Stream being collected to avoid optimizations that would fail to preserve the order of the stream
     * elements. Note that this collector may not perform as well as collectors that are free to violate encounter order.
     * @param <T> The upstream type
     * @return a Multiset&lt;T&gt;
     */
    @Nonnull
    static <T> Collector<T, ?, Multiset<T>> toLinkedMultiset(int initialDistinctElements) {
        return Helpers.toCollectionWithCapacity(LinkedHashMultiset::create, initialDistinctElements, true);
    }

    /**
     * Collector for collecting to a Guava SortedMultiset, where set contents are in their "natural" order
     * @param <C> Type of element in the collection.  Must be a Comparable.
     * @return a SortedMultiset sorted in the "Natural order"
     */
    @Nonnull
    static <C extends Comparable<C>> Collector<C, ?, SortedMultiset<C>> toSortedMultiset() {
        return Helpers.toCollection(TreeMultiset::create, false);
    }

    @Nonnull
    static <C extends Comparable<C>> Collector<C, ?, SortedMultiset<C>> toSortedMultiset(@Nonnull Comparator<C> comparator) {
        return Helpers.toCollectionWithComparator(TreeMultiset::create, comparator);
    }

    @Nonnull
    static <T, K, V> Collector<T, ?, Multimap<K, V>> toHashMultimap(@Nonnull Function<? super T, ? extends K> keyExtractor,
                                                                    @Nonnull Function<? super T, ? extends V> valueExtractor) {
        return Helpers.toMultimap(keyExtractor, valueExtractor, HashMultimap::create);
    }

    @Nonnull
    static <T, K, V> Collector<T, ?, Multimap<K, V>> toLinkedListMultimap(@Nonnull Function<? super T, ? extends K> keyExtractor,
                                                                          @Nonnull Function<? super T, ? extends V> valueExtractor) {
        return Helpers.toMultimap(keyExtractor, valueExtractor, LinkedListMultimap::create);
    }

    @Nonnull
    static <T, K, V> Collector<T, ?, Multimap<K, V>> toLinkedHashMultimap(@Nonnull Function<? super T, ? extends K> keyExtractor,
                                                                          @Nonnull Function<? super T, ? extends V> valueExtractor) {
        return Helpers.toMultimap(keyExtractor, valueExtractor, LinkedHashMultimap::create);
    }

    @Nonnull
    static <T, K extends Comparable<K>, V extends Comparable<V>> Collector<T, ?, Multimap<K, V>> toSortedMultimap(
            @Nonnull Function<? super T, ? extends K> keyExtractor,
            @Nonnull Function<? super T, ? extends V> valueExtractor) {
        return Helpers.toMultimap(keyExtractor, valueExtractor, TreeMultimap::create);
    }

    @Nonnull
    static <T, K extends Comparable<K>, V extends Comparable<V>> Collector<T, ?, Multimap<K, V>> toSortedMultimap(
            @Nonnull Function<? super T, ? extends K> keyExtractor,
            @Nonnull Function<? super T, ? extends V> valueExtractor,
            @Nonnull Comparator<K> keyComparator,
            @Nonnull Comparator<V> valueComparator) {
        return Helpers.toMultimap(keyExtractor, valueExtractor, () -> TreeMultimap.create(keyComparator, valueComparator));
    }

    @Nonnull
    static <T, R, C, V> Collector<T, ?, Table<R, C, V>> toTable(@Nonnull Function<? super T, ? extends R> rowKeyExtractor,
                                                                @Nonnull Function<? super T, ? extends C> columnKeyExtractor,
                                                                @Nonnull Function<? super T, ? extends V> cellValueExtractor) {

        return Helpers.toTable(rowKeyExtractor, columnKeyExtractor, cellValueExtractor, HashBasedTable::create);
    }

    @Nonnull
    static <T, R extends Comparable<R>, C extends Comparable<C>, V>
    Collector<T, ?, RowSortedTable<R, C, V>> toRowSortedTable(@Nonnull Function<? super T, ? extends R> rowKeyExtractor,
                                                              @Nonnull Function<? super T, ? extends C> columnKeyExtractor,
                                                              @Nonnull Function<? super T, ? extends V> cellValueExtractor) {

        return Helpers.toTable(rowKeyExtractor, columnKeyExtractor, cellValueExtractor, TreeBasedTable::create);
    }

    // there is no concept of "private" in an interface so helper functions that would be private if this were a class go in this sub-interface.
    // Some of them are useful in their own right, so having them exposed is not a bad thing.
    interface Helpers {
        // the use of this set of characteristics tells the Stream that it must preserve encounter order so that the same
        // ordering found in stream's source will be preserved in the collected output.  This can affect performance because
        // it will prevent steams that come from ordered collections from being parallelized in order to preserve that order.
        Characteristics[] ORDERED_ID_FINISH = {Characteristics.IDENTITY_FINISH};

        // the use of this set of characteristics tells the Stream that it is ok to perform optimizations (like parallelization)
        // that would mess up the encounter order of an ordered data source.
        Characteristics[] UNORDERED_ID_FINISH = {Characteristics.IDENTITY_FINISH,
                Characteristics.UNORDERED};

        // the use of this set of characteristics tells the Stream that it is ok to perform optimizations (like parallelization)
        // that would mess up the encounter order of an ordered data source, and that multiple threads can deposit into it directly.
        Characteristics[] UNORDERED_ID_FINISH_CONCURRENT = {Characteristics.IDENTITY_FINISH,
                Characteristics.UNORDERED, Characteristics.CONCURRENT};

        @Nonnull
        static <T, C extends Collection<T>> Collector<T, ?, C> toCollection(@Nonnull Supplier<C> factory, boolean upstreamMustPreserveOrder) {
            return Collector.of(
                    factory,
                    Collection::add,
                    mergeWith(Collection::addAll),
                    upstreamMustPreserveOrder ? ORDERED_ID_FINISH : UNORDERED_ID_FINISH);
        }

        @Nonnull
        static <T, C extends Collection<T>> Collector<T, ?, C> toCollectionWithCapacity(@Nonnull IntFunction<C> factory,
                                                                                        int initialCapacity,
                                                                                        boolean upstreamMustPreserveOrder) {
            return Collector.of(
                    (Supplier<C>) () -> factory.apply(initialCapacity),
                    Collection::add,
                    mergeWith(Collection::addAll),
                    upstreamMustPreserveOrder ? ORDERED_ID_FINISH : UNORDERED_ID_FINISH);
        }


        @Nonnull
        static <T, K, V> Collector<T, ?, Multimap<K, V>> toMultimap(@Nonnull Function<? super T, ? extends K> keyExtractor,
                                                                    @Nonnull Function<? super T, ? extends V> valueExtractor,
                                                                    @Nonnull Supplier<Multimap<K, V>> factory) {
            return Collector.of(
                    factory,
                    (m, t) -> m.put(keyExtractor.apply(t), valueExtractor.apply(t)),
                    mergeWith(Multimap::putAll));
        }

        @Nonnull
        static <T, K, V> Collector<T, ?, Multimap<K, V>> toListMultimap(@Nonnull Function<? super T, ? extends K> keyExtractor,
                                                                        @Nonnull Function<? super T, ? extends V> valueExtractor,
                                                                        @Nonnull Supplier<Map<K, Collection<V>>> backingMapFactory,
                                                                        @Nonnull Supplier<List<V>> valueCollectionFactory) {
            Supplier<Multimap<K, V>> multimapSupplier =
                    () -> Multimaps.newListMultimap(backingMapFactory.get(), valueCollectionFactory::get);
            return toMultimap(keyExtractor, valueExtractor,
                    multimapSupplier);
        }

        @Nonnull
        static <T, K, V> Collector<T, ?, Multimap<K, V>> toSetMultimap(@Nonnull Function<? super T, ? extends K> keyExtractor,
                                                                       @Nonnull Function<? super T, ? extends V> valueExtractor,
                                                                       @Nonnull Supplier<Map<K, Collection<V>>> backingMapFactory,
                                                                       @Nonnull Supplier<Set<V>> valueCollectionFactory) {
            Supplier<Multimap<K, V>> multimapSupplier =
                    () -> Multimaps.newSetMultimap(backingMapFactory.get(), valueCollectionFactory::get);
            return toMultimap(keyExtractor, valueExtractor,
                    multimapSupplier);
        }

        @Nonnull
        static <T, K, V> Collector<T, ?, Multimap<K, V>> toSortedSetMultimap(@Nonnull Function<? super T, ? extends K> keyExtractor,
                                                                             @Nonnull Function<? super T, ? extends V> valueExtractor,
                                                                             @Nonnull Supplier<Map<K, Collection<V>>> backingMapFactory,
                                                                             @Nonnull Supplier<SortedSet<V>> valueCollectionFactory) {
            Supplier<Multimap<K, V>> multimapSupplier =
                    () -> Multimaps.newSortedSetMultimap(backingMapFactory.get(), valueCollectionFactory::get);
            return toMultimap(keyExtractor, valueExtractor,
                    multimapSupplier);
        }

        @Nonnull
        static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(@Nonnull Function<? super T, ? extends K> keyFunc,
                                                                            @Nonnull Function<? super T, ? extends V> valFunc) {
            return Collector.of(
                    ImmutableMap::builder,
                    (Builder<K, V> b, T t) -> b.put(keyFunc.apply(t), valFunc.apply(t)),
                    (b1, b2) -> b1.putAll(b2.build()),
                    Builder::build);
        }

        @Nonnull
        static <T, R, C, V, M extends Table<R, C, V>> Collector<T, ?, M> toTable(@Nonnull Function<? super T, ? extends R> rowKeyExtractor,
                                                                                 @Nonnull Function<? super T, ? extends C> columnKeyExtractor,
                                                                                 @Nonnull Function<? super T, ? extends V> cellValueExtractor,
                                                                                 @Nonnull Supplier<M> factory) {
            return Collector.of(
                    factory,
                    (M m, T t) -> m.put(rowKeyExtractor.apply(t), columnKeyExtractor.apply(t), cellValueExtractor.apply(t)),
                    mergeWith(M::putAll)
            );
        }

        @Nonnull
        static <T, R, C> Collector<T, ?, Table<R, C, List<T>>> groupingToTable(@Nonnull Function<? super T, ? extends R> rowKeyExtractor,
                                                                               @Nonnull Function<? super T, ? extends C> columnKeyExtractor) {
            return groupingToTable(rowKeyExtractor, columnKeyExtractor, HashBasedTable::create, Collectors.toList());
        }

        @Nonnull
        static <T, R, C, A, D> Collector<T, ?, Table<R, C, D>> groupingToTable(@Nonnull Function<? super T, ? extends R> rowKeyExtractor,
                                                                               @Nonnull Function<? super T, ? extends C> columnKeyExtractor,
                                                                               @Nonnull Collector<? super T, A, D> downstream) {
            return groupingToTable(rowKeyExtractor, columnKeyExtractor, HashBasedTable::create, downstream);
        }

        @Nonnull
        static <T, R, C, D, A, M extends Table<R, C, D>> Collector<T, ?, M> groupingToTable(@Nonnull Function<? super T, ? extends R> rowKeyExtractor,
                                                                                            @Nonnull Function<? super T, ? extends C> columnKeyExtractor,
                                                                                            @Nonnull Supplier<M> factory,
                                                                                            @Nonnull Collector<? super T, A, D> downstream) {

            Supplier<A> downstreamSupplier = downstream.supplier();
            BiConsumer<A, ? super T> downstreamAccumulator = downstream.accumulator();
            BiConsumer<Table<R, C, A>, T> accumulator = (m, t) -> {
                R rowKey = Preconditions.checkNotNull(rowKeyExtractor.apply(t), "row key cannot be null");
                C colKey = Preconditions.checkNotNull(columnKeyExtractor.apply(t), "column key cannot be null");
                A container;
                if ((container = m.get(rowKey, colKey)) == null) {
                    A newContainer;
                    if ((newContainer = downstreamSupplier.get()) != null) {
                        m.put(rowKey, colKey, newContainer);
                        container = newContainer;
                    }
                }
                downstreamAccumulator.accept(container, t);
            };
            BinaryOperator<Table<R, C, A>> merger = mergeWith(Table::putAll);

            @SuppressWarnings("unchecked")
            Supplier<Table<R, C, A>> mangledFactory = (Supplier<Table<R, C, A>>) factory;

            if (downstream.characteristics().contains(Characteristics.IDENTITY_FINISH)) {
                return Collector.of(mangledFactory, accumulator, merger, m -> (M) m, ORDERED_ID_FINISH);
            } else {
                @SuppressWarnings("unchecked")
                Function<? super A, ? extends A> downstreamFinisher = (Function<? super A, ? extends A>) downstream.finisher();
                Function<Table<R, C, A>, M> finisher = intermediate -> {
                    intermediate.cellSet().forEach(cell -> intermediate.put(cell.getRowKey(), cell.getColumnKey(), downstreamFinisher.apply(cell.getValue())));
                    @SuppressWarnings("unchecked")
                    M castResult = (M) intermediate;
                    return castResult;
                };
                return Collector.of(mangledFactory, accumulator, merger, finisher, ORDERED_ID_FINISH);
            }
        }

        @Nonnull
        static <C> BinaryOperator<C> mergeWith(@Nonnull BiConsumer<C, C> merger) {
            return (receiver, from) -> {
                merger.accept(receiver, from);
                return receiver;
            };
        }

        @Nonnull
        static <T, C extends Collection<T>> Collector<T, ?, C> toCollectionWithComparator(@Nonnull Function<Comparator<T>, C> factory,
                                                                                          @Nonnull Comparator<T> comparator) {
            return toCollection(() -> factory.apply(comparator), false);
        }

        @Nonnull
        static <T> T alwaysThrow(@Nonnull T t1, @Nonnull T t2) {
            throw new IllegalStateException("Duplicates not allowed");
        }

    }
}
