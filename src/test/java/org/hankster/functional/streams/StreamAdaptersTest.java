package org.hankster.functional.streams;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;

public class StreamAdaptersTest {

    @Test
    public void testFromFluentIterable() throws Exception {
        FluentIterable<String> fluentIterable = FluentIterable.from(Lists.newArrayList("a", "A", "b", "B"));
        Set<String> result = StreamAdapters.fromFluentIterable(fluentIterable)
                .filter(s -> s.equalsIgnoreCase("A"))
                .collect(toSet());
        assertTrue(result.contains("A"));
        assertTrue(result.contains("a"));
        assertFalse(result.contains("B"));
        assertFalse(result.contains("b"));
    }

    @Test
    public void testToFluentIterable() throws Exception {
        Stream<String> stream = Stream.of("a","A","b","B");
        Set<String> result = StreamAdapters.toFluentIterable(stream)
                .filter(s -> s.equalsIgnoreCase("A"))
                .toSet();
        assertTrue(result.contains("A"));
        assertTrue(result.contains("a"));
        assertFalse(result.contains("B"));
        assertFalse(result.contains("b"));
    }
}