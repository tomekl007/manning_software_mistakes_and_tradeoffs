package com.tomekl007.CH03;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompletableFutureTest {
  private static final Logger logger = LoggerFactory.getLogger(CompletableFutureTest.class);

  @Test
  public void shouldWrapActualExceptionIntCompletionExceptionAndMultipleIndirectCalls() {
    CompletableFuture<Integer> result = new AsyncService().asyncExternalCall();

    result.whenComplete(
        (BiConsumer<Object, Throwable>)
            (o, throwable) -> {
              if (throwable != null) {
                logger.error("Exception in async processing", throwable);
              }
            });
    assertThatThrownBy(result::get)
        .hasCauseInstanceOf(RuntimeException.class)
        .hasRootCauseExactlyInstanceOf(IOException.class);
  }

  @Test
  public void shouldReturnOnlyRootCauseOfAnException() {
    CompletableFuture<Integer> result = new AsyncService().asyncExternalCallImproved();

    result.whenComplete(
        (BiConsumer<Object, Throwable>)
            (o, throwable) -> {
              if (throwable != null) {
                logger.error("Exception in async processing", throwable);
              }
            });
    assertThatThrownBy(result::get)
        .hasCauseInstanceOf(IOException.class)
        .hasRootCauseExactlyInstanceOf(IOException.class);
  }
}
