package com.tomekl007.CH05.premature;

import com.tomekl007.CH05.Account;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@Fork(1)
@Warmup(iterations = 1)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class AccountsFinderPerformanceBenchmark {
  private static final List<Account> ACCOUNTS =
      IntStream.range(0, 10_000)
          .boxed()
          .map(v -> new Account(UUID.randomUUID().toString(), v))
          .collect(Collectors.toList());
  private static final Random random = new Random();

  @Benchmark
  public void baseline(Blackhole blackhole) {
    Optional<Account> account = new AccountFinder(ACCOUNTS).account(random.nextInt(10_000));
    blackhole.consume(account);
  }

  @Benchmark
  public void parallel(Blackhole blackhole) {
    Optional<Account> account =
        new AccountFinder(ACCOUNTS).accountOptimized(random.nextInt(10_000));
    blackhole.consume(account);
  }
}
