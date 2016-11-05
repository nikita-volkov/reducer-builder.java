package com.github.nikita_volkov.java.reducer_builder;

import com.github.nikita_volkov.java.composites.Product2;
import com.github.nikita_volkov.java.iterables.*;
import com.github.nikita_volkov.java.reducer.*;

import java.util.*;
import java.util.function.*;

public final class ReducerBuilder<input, output> {

  public final Reducer<input, output> reducer;

  public ReducerBuilder(Reducer<input, output> reducer) {
    this.reducer = reducer;
  }

  public <output2> ReducerBuilder<input, output2> compose(ReducerBuilder<output, output2> otherBuilder) {
    return new ReducerBuilder<>(new ReducingReducer<>(reducer, otherBuilder.reducer));
  }

  public <input2> ReducerBuilder<input2, output> prereduce(ReducerBuilder<input2, input> otherBuilder) {
    return new ReducerBuilder<>(new ReducingReducer<>(otherBuilder.reducer, reducer));
  }

  public <input2> ReducerBuilder<input2, output> preiterate(ReducerBuilder<input2, Iterable<input>> otherBuilder) {
    return chain(otherBuilder, this);
  }

  public <output2> ReducerBuilder<input, output2> map(Function<output, output2> fn) {
    return new ReducerBuilder<>(new MappingReducer<>(reducer, fn));
  }

  public <input2> ReducerBuilder<input2, output> preflatmap(Function<input2, Iterable<input>> fn) {
    return new ReducerBuilder<>(new ContraflatmappingReducer<>(reducer, fn));
  }

  public <input2> ReducerBuilder<input2, output> premap(Function<input2, input> fn) {
    return new ReducerBuilder<>(new ContramappingReducer<>(reducer, fn));
  }

  public ReducerBuilder<input, output> prefilter(Predicate<input> predicate) {
    return new ReducerBuilder<>(new FilteringReducer<>(reducer, predicate));
  }

  public ReducerBuilder<input, output> preintersperse(input separator) {
    return new ReducerBuilder<>(new InterspersingReducer<>(reducer, separator));
  }

  public ReducerBuilder<input, output> pretake(long amount) {
    return new ReducerBuilder<>(new TakingReducer<>(reducer, amount));
  }

  public ReducerBuilder<input, output> predrop(long amount) {
    return new ReducerBuilder<>(new DroppingReducer<>(reducer, amount));
  }

  public <output2> ReducerBuilder<input, Product2<output, output2>> zip(ReducerBuilder<input, output2> otherReducerBuilder) {
    return new ReducerBuilder<>(new ZippingReducer<>(reducer, otherReducerBuilder.reducer));
  }

  public static final ReducerBuilder<Character, String> charCat =
    new ReducerBuilder<>(new CharacterCatReducer());

  public static final ReducerBuilder<String, String> stringCat =
    new ReducerBuilder<>(new StringCatReducer());

  public static final ReducerBuilder<Long, Long> longSum =
    new ReducerBuilder<>(new LongSumReducer());

  public static final ReducerBuilder<Integer, Integer> intSum =
    new ReducerBuilder<>(new FoldReducer<>(0, (a, b) -> a + b));

  public static final ReducerBuilder<Double, Double> doubleSum =
    new ReducerBuilder<>(new FoldReducer<>(0D, (a, b) -> a + b));

  public static final ReducerBuilder<Float, Float> floatSum =
    new ReducerBuilder<>(new FoldReducer<>(0F, (a, b) -> a + b));

  public static <a, b, c> ReducerBuilder<a, c> chain(ReducerBuilder<a, Iterable<b>> builder1, ReducerBuilder<b, c> builder2) {
    return new ReducerBuilder<>(new ChainingReducer<>(builder1.reducer, builder2.reducer));
  }

  public static <input, accumulator> ReducerBuilder<input, accumulator> fold(accumulator init, BiFunction<accumulator, input, accumulator> step) {
    return new ReducerBuilder<>(new FoldReducer<>(init, step));
  }

  private static final ReducerBuilder length =
    new ReducerBuilder<>(new LengthReducer<>());

  public static <input> ReducerBuilder<input, Long> length() {
    return length;
  }

  private static final ReducerBuilder linkedList =
    new ReducerBuilder<>(new LinkedListReducer<>());

  public static <input> ReducerBuilder<input, LinkedList<input>> linkedList() {
    return linkedList;
  }

  private static final ReducerBuilder max =
    new ReducerBuilder(new MaxReducer());

  public static <input extends Comparable<input>> ReducerBuilder<input, Optional<input>> max() {
    return max;
  }

  private static final ReducerBuilder min =
    new ReducerBuilder(new MinReducer());

  public static <input extends Comparable<input>> ReducerBuilder<input, Optional<input>> min() {
    return min;
  }

  public static <input, output> ReducerBuilder<input, Iterable<output>> zipMany(Iterable<ReducerBuilder<input, output>> iterations) {
    return new ReducerBuilder<>(new ZippingManyReducer<>(new MappingIterable<>(iterations, iteration -> iteration.reducer)));
  }

  public static <input, output> ReducerBuilder<input, Iterable<output>> zipMany(ReducerBuilder<input, output>... iterations) {
    return zipMany(new ArrayIterable<>(iterations));
  }

  public static <input, output1, output2> ReducerBuilder<input, Product2<output1, output2>> zip(ReducerBuilder<input, output1> builder1, ReducerBuilder<input, output2> builder2) {
    return new ReducerBuilder<>(new ZippingReducer<>(builder1.reducer, builder2.reducer));
  }

}
