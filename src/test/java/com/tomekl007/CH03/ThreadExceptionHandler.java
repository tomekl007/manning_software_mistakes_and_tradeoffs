package com.tomekl007.CH03;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(ThreadExceptionHandler.class);

  @Test
  public void shouldPropagateException() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Runnable r =
        () -> {
          throw new RuntimeException("problem");
        };
    Future<?> submit = executorService.submit(r);
    assertThatThrownBy(submit::get)
        .hasRootCauseExactlyInstanceOf(RuntimeException.class)
        .hasMessageContaining("problem");
  }

  @Test
  public void shouldSilentlyDiscardWhenUsingSubmit() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Runnable r =
        () -> {
          throw new RuntimeException("problem");
        };
    executorService.submit(r);
  }

  @Test
  public void shouldPropagateInLogsWhenUsingExecute() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Runnable r =
        () -> {
          throw new RuntimeException("problem");
        };
    executorService.execute(r);
  }

  @Test
  public void shouldUseUncaughtExceptionHandlerWhenExecute() {
    // given
    AtomicBoolean uncaughtExceptionHandlerCalled = new AtomicBoolean();
    ThreadFactory factory =
        r -> {
          final Thread thread = new Thread(r);
          thread.setUncaughtExceptionHandler(
              (t, e) -> {
                uncaughtExceptionHandlerCalled.set(true);
                logger.error("Exception in thread: " + t, e);
              });
          return thread;
        };

    Runnable task =
        () -> {
          throw new RuntimeException("problem");
        };
    ExecutorService pool = Executors.newSingleThreadExecutor(factory);
    // when
    pool.execute(task);
    await().atMost(5, TimeUnit.SECONDS).until(uncaughtExceptionHandlerCalled::get);
  }

  @Test
  public void shouldNotUseUncaughtExceptionHandlerWhenSubmit() {
    // given
    AtomicBoolean uncaughtExceptionHandlerCalled = new AtomicBoolean();
    ThreadFactory factory =
        r -> {
          final Thread thread = new Thread(r);
          thread.setUncaughtExceptionHandler(
              (t, e) -> {
                uncaughtExceptionHandlerCalled.set(true);
                logger.error("Exception in thread: " + t, e);
              });
          return thread;
        };

    Runnable task =
        () -> {
          throw new RuntimeException("problem");
        };
    ExecutorService pool = Executors.newSingleThreadExecutor(factory);
    // when
    pool.submit(task);
    // will discard silently
    assertThat(uncaughtExceptionHandlerCalled.get()).isFalse();
  }

  @Test
  public void shouldNotUseUncaughtExceptionHandlerWhenSubmitAndGet() {
    // given
    AtomicBoolean uncaughtExceptionHandlerCalled = new AtomicBoolean();
    ThreadFactory factory =
        r -> {
          final Thread thread = new Thread(r);
          thread.setUncaughtExceptionHandler(
              (t, e) -> {
                uncaughtExceptionHandlerCalled.set(true);
                logger.error("Exception in thread: " + t, e);
              });
          return thread;
        };

    Runnable task =
        () -> {
          throw new RuntimeException("problem");
        };
    ExecutorService pool = Executors.newSingleThreadExecutor(factory);
    // when
    Future<?> submit = pool.submit(task);
    // will fail fast
    assertThatThrownBy(submit::get)
        .hasRootCauseExactlyInstanceOf(RuntimeException.class)
        .hasMessageContaining("problem");
    assertThat(uncaughtExceptionHandlerCalled.get()).isFalse();
  }
}
