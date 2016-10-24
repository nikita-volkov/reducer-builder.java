package com.github.nikita_volkov.java.reducer_builder;

import com.github.nikita_volkov.java.reducer.Reducer;
import junit.framework.TestCase;

import java.util.Arrays;

public class Test extends TestCase {


  public void testReducerBuilder1() {

    Reducer<String, String> reducer =
      ReducerBuilder.cat
        .preintersperse(",")
        .<String>premap(String::toUpperCase)
        .zip(ReducerBuilder.length())
        .pretake(2)
        .prefilter(x -> !x.equals("a"))
        .map(x -> x._1 + "(" + x._2 + ")")
        .reducer;

    assertEquals("B,C(2)", reducer.consume(Arrays.asList("a", "a", "b", "c", "d")));

  }

  public void testReducerBuilder2() {

    Reducer<String, String> reducer =
      ReducerBuilder.zip(ReducerBuilder.cat.preintersperse(","), ReducerBuilder.length())
        .map(x -> x._1 + "(" + x._2.toString() + ")")
        .<String>premap(String::toUpperCase)
        .pretake(2)
        .prefilter(x -> !x.equals("a"))
        .reducer;

    assertEquals("B,C(2)", reducer.consume(Arrays.asList("a", "a", "b", "c", "d")));

  }

  public void testReducerBuilder3() {
    Reducer<Long, Long> reducer =
      ReducerBuilder.longSum.preiterate(ReducerBuilder.zipMany(ReducerBuilder.longSum, ReducerBuilder.length())).reducer;

    assertEquals(6L, reducer.consume(Arrays.asList(1L, 1L, 1L)).longValue());

  }

}