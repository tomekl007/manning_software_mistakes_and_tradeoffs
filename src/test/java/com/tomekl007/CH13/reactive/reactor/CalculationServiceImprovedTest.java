package com.tomekl007.CH13.reactive.reactor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

class CalculationServiceImprovedTest {
  @Test
  public void shouldCalculateNElements() {
    // given
    CalculationServiceImproved calculationService = new CalculationServiceImproved();
    List<Integer> input = IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList());
    // when
    Flux<Integer> resultFlux = calculationService.calculateForUserIds(input);
    List<Integer> result = resultFlux.collect(Collectors.toList()).block();

    // then
    assertThat(result.size()).isEqualTo(10);
  }
}
