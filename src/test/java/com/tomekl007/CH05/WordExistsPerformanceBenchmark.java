package com.tomekl007.CH05;

import com.tomekl007.CH05.initial.DefaultWordsService;
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

@Fork(1)
@Warmup(iterations = 1)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class WordExistsPerformanceBenchmark {
  private static final List<String> WORDS_TO_CHECK =
      Arrays.asList("made", "ask", "find", "zones", "1ask", "123");

  @Benchmark
  public void baseline(Blackhole blackhole) {
    DefaultWordsService defaultWordsService = new DefaultWordsService(getWordsPath());
    for (String word : WORDS_TO_CHECK) {
      blackhole.consume(defaultWordsService.wordExists(word));
    }
  }

  private Path getWordsPath() {
    return Paths.get(
        Objects.requireNonNull(getClass().getClassLoader().getResource("words.txt")).getPath());
  }
}
