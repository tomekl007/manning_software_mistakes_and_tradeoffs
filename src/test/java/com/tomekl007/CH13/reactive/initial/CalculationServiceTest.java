package com.tomekl007.CH13.reactive.initial;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class CalculationServiceTest {

  @Test
  public void shouldCalculateNElements() {
    // given
    CalculationService calculationService = new CalculationService();
    List<Integer> input = IntStream.range(1, 11).boxed().collect(Collectors.toList());
    // when
    List<Integer> result = calculationService.calculateForUserIds(input);

    // then
    assertThat(result.size()).isEqualTo(10);
  }
}
