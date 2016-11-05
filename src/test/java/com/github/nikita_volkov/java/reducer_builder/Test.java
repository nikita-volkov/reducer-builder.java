package com.github.nikita_volkov.java.reducer_builder;

import com.github.nikita_volkov.java.reducer.*;
import com.github.nikita_volkov.java.reducer.reducible.ArrayReducible;
import junit.framework.TestCase;

public class Test extends TestCase {

  public void testReducerBuilder1() {

    Reducer<String, String> reducer =
      ReducerBuilder.stringCat
        .preintersperse(",")
        .<String>premap(String::toUpperCase)
        .zip(ReducerBuilder.length())
        .pretake(2)
        .prefilter(x -> !x.equals("a"))
        .map(x -> x._1 + "(" + x._2 + ")")
        .reducer;

    assertEquals("B,C(2)", new ArrayReducible<>("a", "a", "b", "c", "d").reduce(reducer));

  }

  public void testReducerBuilder2() {

    Reducer<String, String> reducer =
      ReducerBuilder.zip(ReducerBuilder.stringCat.preintersperse(","), ReducerBuilder.length())
        .map(x -> x._1 + "(" + x._2.toString() + ")")
        .<String>premap(String::toUpperCase)
        .pretake(2)
        .prefilter(x -> !x.equals("a"))
        .reducer;

    assertEquals("B,C(2)", new ArrayReducible<>("a", "a", "b", "c", "d").reduce(reducer));

  }

  public void testReducerBuilder3() {
    Reducer<Long, Long> reducer =
      ReducerBuilder.longSum.preiterate(ReducerBuilder.zipMany(ReducerBuilder.longSum, ReducerBuilder.length())).reducer;

    assertEquals(6L, new ArrayReducible<>(1L, 1L, 1L).reduce(reducer).longValue());

  }

  public void testReducerBuilder4() {

    Reducer<Character, String> reducer =
      ReducerBuilder.stringCat
        .prereduce(ReducerBuilder.<Character>length().pretake(2).map(Object::toString))
        .reducer;

    assertEquals("221", new ArrayReducible<>('a', 'b', 'c', 'd', 'e').reduce(reducer));

  }

}