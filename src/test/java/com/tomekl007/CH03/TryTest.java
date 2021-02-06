package com.tomekl007.CH03;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

public class TryTest {

  @Test
  public void shouldHandleSuccess() {
    // given
    String defaultResult = "default";
    Supplier<Integer> clientAction = () -> 100;

    // when
    Try<Integer> response = Try.ofSupplier(clientAction);
    String result = response.map(Object::toString).getOrElse(defaultResult);

    // then
    assertTrue(response.isSuccess());
    response.onSuccess(r -> assertThat(r).isEqualTo(100));
    assertThat(result).isEqualTo("100");
  }

  @Test
  public void shouldHandleFailure() {
    // given
    String defaultResult = "default";
    Supplier<Integer> clientAction =
        () -> {
          throw new RuntimeException("problem");
        };

    // when
    Try<Integer> response = Try.ofSupplier(clientAction);
    String result = response.map(Object::toString).getOrElse(defaultResult);
    Option<Integer> optionalResponse = response.toOption();

    // then
    assertTrue(optionalResponse.isEmpty());
    assertTrue(response.isFailure());
    assertThat(result).isEqualTo(defaultResult);
    response.onSuccess(r -> assertThat(r).isEqualTo(100));
    response.onFailure(ex -> assertTrue(ex instanceof RuntimeException));
  }

  @Test
  public void shouldRecoverFromProblem() {
    // given
    Supplier<Integer> clientAction =
        () -> {
          throw new RuntimeException("problem");
        };
    int defaultResult = 0;
    // when
    Try<Integer> recovered =
        Try.ofSupplier(clientAction).recover(RuntimeException.class, e -> defaultResult);

    // then
    assertTrue(recovered.isSuccess());
  }
}
