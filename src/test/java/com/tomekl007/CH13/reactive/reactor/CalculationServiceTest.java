package com.tomekl007.CH13.reactive.reactor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

class CalculationServiceTest {
  @Test
  public void shouldCalculateNElements() {
    // given
    CalculationService calculationService = new CalculationService();
    List<Integer> input = IntStream.range(1, 11).boxed().collect(Collectors.toList());
    // when
    Flux<Integer> resultFlux = calculationService.calculateForUserIds(input);
    List<Integer> result = resultFlux.collect(Collectors.toList()).block();

    // then
    assertThat(result.size()).isEqualTo(10);
  }
}
