package com.tomekl007.CH01;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@Fork(1)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
@BenchmarkMode(Mode.AverageTime)
@Threads(100)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchmarkSingletonVsThreadLocal {
  private static final int NUMBER_OF_ITERATIONS = 50_000;

  @Benchmark
  public void singletonWithSynchronization(Blackhole blackhole) {
    for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
      blackhole.consume(SystemComponentSingletonSynchronized.getInstance());
    }
  }

  @Benchmark
  public void singletonWithDoubleCheckedLocking(Blackhole blackhole) {
    for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
      blackhole.consume(SystemComponentSingletonDoubleCheckedLocking.getInstance());
    }
  }

  @Benchmark
  public void singletonWithThreadLocal(Blackhole blackhole) {
    for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
      blackhole.consume(SystemComponentThreadLocal.get());
    }
  }
}
