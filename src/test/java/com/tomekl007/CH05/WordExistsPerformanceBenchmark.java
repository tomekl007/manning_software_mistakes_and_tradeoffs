package com.tomekl007.CH05;

import com.tomekl007.CH05.cache.CachedWordsService;
import com.tomekl007.CH05.initial.DefaultWordsService;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * To start this benchmark, run the {@link WordExistsPerformanceTestRunner#main(String[])} method}
 */
@Fork(1)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class WordExistsPerformanceBenchmark {
  private static final int NUMBER_OF_CHECKS = 100;
  private static final List<String> WORDS_TO_CHECK =
      Arrays.asList("made", "ask", "find", "zones", "1ask", "123");

  @Benchmark
  public void baseline(Blackhole blackhole) {
    WordsService defaultWordsService = new DefaultWordsService(getWordsPath());
    for (int i = 0; i < NUMBER_OF_CHECKS; i++) {
      for (String word : WORDS_TO_CHECK) {
        blackhole.consume(defaultWordsService.wordExists(word));
      }
    }
  }

  @Benchmark
  public void cache(Blackhole blackhole) {
    WordsService defaultWordsService = new CachedWordsService(getWordsPath());
    for (int i = 0; i < NUMBER_OF_CHECKS; i++) {
      for (String word : WORDS_TO_CHECK) {
        blackhole.consume(defaultWordsService.wordExists(word));
      }
    }
  }

  private Path getWordsPath() {
    try {
      return Paths.get(
          Objects.requireNonNull(getClass().getClassLoader().getResource("words.txt")).toURI());
    } catch (URISyntaxException e) {
      throw new IllegalStateException("Invalid words.txt path", e);
    }
  }
}
