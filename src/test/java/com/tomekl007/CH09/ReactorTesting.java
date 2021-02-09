package com.tomekl007.CH09;

import static com.tomekl007.CH09.ReactorCode.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.Duration;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

public class ReactorTesting {
  @Test
  public void testReactorWithoutLibrary() throws InterruptedException {
    // given
    long start = System.currentTimeMillis();
    Flux<Integer> data = Flux.fromIterable(Arrays.asList(1, 2, 3));
    // using Thread.sleep in test is a bad pattern, see testReactorUsingTestingLibrary() for an
    // improvement.
    Thread.sleep(10_000);
    // how to verify that the value after 10 seconds is not taken into account?

    // when
    Flux<Integer> result = sumElementsWithinTimeWindow(data);

    // then
    assertThat(result.blockFirst()).isEqualTo(6);

    System.out.println("end:" + (System.currentTimeMillis() - start));
  }

  @Test
  public void testReactorUsingTestingLibrary() {
    long start = System.currentTimeMillis();

    final TestPublisher<Integer> testPublisher = TestPublisher.create();

    Flux<Integer> result = sumElementsWithinTimeWindow(testPublisher.flux());

    StepVerifier.create(result)
        .then(() -> testPublisher.emit(1, 2, 3))
        .thenAwait(Duration.ofSeconds(10))
        .then(() -> testPublisher.emit(4))
        .expectNext(6)
        .verifyComplete();

    System.out.println("end:" + (System.currentTimeMillis() - start));
  }
}
