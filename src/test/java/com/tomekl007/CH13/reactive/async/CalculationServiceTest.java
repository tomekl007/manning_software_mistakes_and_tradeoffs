package com.tomekl007.CH13.reactive.async;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class CalculationServiceTest {

  @Test
  public void shouldCalculateNElementsAsync() throws ExecutionException, InterruptedException {
    // given
    CalculationService calculationService = new CalculationService();
    List<Integer> input = IntStream.range(1, 11).boxed().collect(Collectors.toList());

    // when
    List<CompletableFuture<Integer>> resultAsync = calculationService.calculateForUserIds(input);
    List<Integer> result = new ArrayList<>(resultAsync.size());

    for (CompletableFuture<Integer> asyncAction : resultAsync) {
      result.add(asyncAction.get());
    }

    // then
    assertThat(result.size()).isEqualTo(10);
  }
}
