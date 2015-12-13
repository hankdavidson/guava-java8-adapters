package org.hankster.functional.streams;

import com.google.common.collect.BiMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Table;
import org.junit.Test;

import java.lang.Character.UnicodeBlock;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hankster.functional.streams.MoreCollectors.*;
import static org.junit.Assert.*;

public class MoreCollectorsTest {

    @Test
    public void testToBiMap() throws Exception {
        BiMap<Integer, Integer> toPow2Map = IntStream.range(0, 32).boxed().collect(toBiMap(Function.identity(), i -> 1 << i));
        assertEquals(256,(int) toPow2Map.get(8));
        BiMap<Integer, Integer> inverse = toPow2Map.inverse();
        assertEquals(8, (int) inverse.get(256));
    }

    @Test
    public void testToMultiset() throws Exception {
        Multiset<String> multiset = Stream.of("A","B","B","C","C","C","D","D","D","D").collect(toMultiset());
        assertEquals(3,multiset.count("C"));
    }

    @Test
    public void testToMultisetPresized() throws Exception {
        Multiset<String> multiset = Stream.of("A","B","B","C","C","C","D","D","D","D").collect(toMultiset(10));
        assertEquals(3,multiset.count("C"));
    }

    @Test
    public void testToConcurrentMultiset() throws Exception {
        Multiset<String> multiset = new Random().ints(100000, 0, 10).boxed().parallel()
                .collect(Collectors.mapping(i -> Thread.currentThread().getName() + ':' + i, toConcurrentMultiset()));  // range of possible values becomes 10 * # of threads
        assertTrue(multiset.elementSet().size() > 10); // shows that there is more than one thread feeding into the collector, indicating that toConcurrentMultiset doesn't disable parallel
        assertEquals(100000,multiset.size());
    }

    @Test
    public void testToLinkedMultiset() throws Exception {
        Multiset<String> multiset = Stream.of("C","A","B","A","B","C","C","A","A").collect(toLinkedMultiset());
        assertEquals(4,multiset.count("A"));
        Iterator<String> iterator = multiset.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("C", iterator.next());
    }

    @Test
    public void testToSortedMultiset() throws Exception {
        Multiset<String> multiset = Stream.of("C","A","B","A","B","C","C","A","A").collect(toSortedMultiset());
        assertEquals(4,multiset.count("A"));
        Iterator<String> iterator = multiset.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("A", iterator.next());
    }

    @Test
    public void testToSortedMultisetWithComparator() throws Exception {
        Multiset<String> multiset = Stream.of("C","A","B","a").collect(toSortedMultiset(String.CASE_INSENSITIVE_ORDER));
        assertEquals(2,multiset.count("A"));
        assertEquals(2,multiset.count("a"));
    }

    @Test
    public void testToLinkedListMultimap() throws Exception {
        Multimap<UnicodeBlock, Integer> unicodeBlockMembers =
                IntStream.range('\u0000', '\uffff')
                        .boxed()
                        .collect(toLinkedListMultimap(UnicodeBlock::of, Function.identity()));

        assertFalse(unicodeBlockMembers.isEmpty());
        assertEquals(128, unicodeBlockMembers.get(UnicodeBlock.BASIC_LATIN).size());
    }

    @Test
    public void testToHashSetMultimap() throws Exception {
        Multimap<UnicodeBlock, Integer> unicodeBlockMembers =
                IntStream.range('\u0000', '\uffff')
                        .boxed()
                        .collect(toHashMultimap(UnicodeBlock::of, Function.identity()));

        assertFalse(unicodeBlockMembers.isEmpty());
        assertEquals(128, unicodeBlockMembers.get(UnicodeBlock.BASIC_LATIN).size());
    }

    @Test
    public void testToLinkedHashSetMultimap() throws Exception {
        Multimap<UnicodeBlock, Integer> unicodeBlockMembers =
                IntStream.range('\u0000', '\uffff')
                        .boxed()
                        .collect(toLinkedHashMultimap(UnicodeBlock::of, Function.identity()));

        assertFalse(unicodeBlockMembers.isEmpty());
        assertEquals(128, unicodeBlockMembers.get(UnicodeBlock.BASIC_LATIN).size());
    }

    @Test
    public void testToSortedMultimap() throws Exception {
        Multimap<String,Integer> unicodeBlockMembers =
                IntStream.range('\u0000', '\uffff')
                        .boxed()
                        .filter(i -> UnicodeBlock.of(i) != null)
                        .collect(toSortedMultimap(i -> UnicodeBlock.of(i).toString(), Function.identity()));

        assertFalse(unicodeBlockMembers.isEmpty());
        assertEquals(128, unicodeBlockMembers.get("BASIC_LATIN").size());
    }

    @Test
    public void testToTable() throws Exception {
        Table<Integer,Integer,Integer> nybbles =
                IntStream.range(0, 0xff)
                        .boxed()
                        .collect(toTable(i -> (i & 0xf0) >> 4, i -> i & 0xf, Function.identity()));

        assertFalse(nybbles.isEmpty());
        assertEquals((Integer) 0xab, nybbles.get(0xa, 0xb));
    }
}
