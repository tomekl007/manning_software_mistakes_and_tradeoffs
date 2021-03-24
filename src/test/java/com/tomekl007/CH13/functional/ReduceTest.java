package com.tomekl007.CH13.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

public class ReduceTest {
  @Test
  public void shouldReduceTwoValues() {
    // given
    List<Integer> input = Arrays.asList(1, 2);

    // when
    Integer result = Reduce.reduce(input, (value, accumulator) -> value + accumulator, 0);

    // then
    assertThat(result).isEqualTo(3);
  }

  @Test
  public void shouldReduceTenValues() {
    // given
    List<Integer> input = IntStream.range(1, 10).boxed().collect(Collectors.toList());

    // when
    Integer result = Reduce.reduce(input, (value, accumulator) -> value + accumulator, 0);

    // then
    assertThat(result).isEqualTo(45);
  }

  @Test
  public void shouldStackOverflowForALotOfValues() {
    // given
    List<Integer> input = IntStream.range(1, 100_000).boxed().collect(Collectors.toList());

    // when
    assertThatThrownBy(() -> Reduce.reduce(input, Integer::sum, 0))
        .isInstanceOf(StackOverflowError.class);
  }
}
