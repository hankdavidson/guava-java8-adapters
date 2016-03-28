package org.hankster.functional.streams;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import org.hankster.functional.functions.UniPredicate;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FunctionAdaptersTest {

    @Test
    public void testUniPredicate() {
        UniPredicate<String> pred = String::isEmpty;

        assertTrue(pred.test(""));
        assertFalse(pred.test("A"));
        assertTrue(pred.apply(""));
        assertFalse(pred.apply("A"));

        com.google.common.base.Predicate guavaPred = pred;
        java.util.function.Predicate java8Pred = pred;

        List<String> listWithEmptyValues = Lists.newArrayList("","A","","B","");
        listWithEmptyValues.removeIf(pred);
        assertEquals(2, listWithEmptyValues.size());

        listWithEmptyValues = Lists.newArrayList("","A","","B","");
        assertFalse(FluentIterable.from(listWithEmptyValues).filter(pred).contains("A"));
    }

    @Test
    public void testUniPredicateDefaultMethods(){
        UniPredicate<String> pred = String::isEmpty;
        UniPredicate<String> negatedPred = pred.negate();

        assertFalse(negatedPred.test(""));
        assertTrue(negatedPred.test("A"));
        assertFalse(negatedPred.apply(""));
        assertTrue(negatedPred.apply("A"));
    }
}
