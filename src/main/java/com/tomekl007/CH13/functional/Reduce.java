package com.tomekl007.CH13.functional;

import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;

public class Reduce {

  public static <T> T reduce(List<T> values, BinaryOperator<T> reducer, T accumulator) {
    return reduceInternal(values, reducer, accumulator);
  }

  private static <T> T reduceInternal(List<T> values, BinaryOperator<T> reducer, T accumulator) {
    if (values.isEmpty()) {
      return accumulator;
    }
    T head = getHead(values);
    List<T> tail = getTail(values);
    T result = reducer.apply(head, accumulator);
    return reduceInternal(tail, reducer, result);
  }

  private static <T> List<T> getTail(List<T> values) {
    if (values.size() == 1) {
      return Collections.emptyList();
    }

    return values.subList(1, values.size());
  }

  private static <T> T getHead(List<T> values) {
    return values.get(0);
  }
}
