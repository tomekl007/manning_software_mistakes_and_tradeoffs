package com.tomekl007.CH05.measure;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.codahale.metrics.Timer;
import com.google.common.annotations.VisibleForTesting;
import com.tomekl007.CH05.WordsService;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.function.Supplier;

/** Metrics are available here: http://localhost:8081/metrics?pretty=true */
public class MeasuredDefaultWordsService implements WordsService {

  private static final int MULTIPLY_FACTOR = 100;
  private static final Supplier<Integer> DEFAULT_INDEX_PROVIDER =
      MeasuredDefaultWordsService::getIndexForToday;

  private Path filePath;

  private Supplier<Integer> indexProvider;

  private MetricRegistry metricRegistry;

  // to see metrics see, http://localhost:8081/metrics?pretty=true
  public MeasuredDefaultWordsService(Path filePath) {
    this(filePath, DEFAULT_INDEX_PROVIDER, SharedMetricRegistries.getDefault());
  }

  @VisibleForTesting
  public MeasuredDefaultWordsService(
      Path filePath, Supplier<Integer> indexProvider, MetricRegistry metricRegistry) {
    this.filePath = filePath;
    this.indexProvider = indexProvider;
    this.metricRegistry = metricRegistry;
  }

  @Override
  public String getWordOfTheDay() {
    int index = indexProvider.get();

    try (Scanner scanner = new Scanner(filePath.toFile())) {
      int i = 0;
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (index == i) {
          return line;
        }
        i++;
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Problem in getWordOfTheDay for index: " + filePath, e);
    }

    return "No word today.";
  }

  @Override
  public boolean wordExists(String word) {
    Timer loadFile = metricRegistry.timer("loadFile");
    try (Scanner scanner = loadFile.time(() -> new Scanner(filePath.toFile()))) {
      // scan is more costly, how to optimize it?
      Timer scan = metricRegistry.timer("scan");
      return scan.time(
          () -> {
            while (scanner.hasNextLine()) {
              String line = scanner.nextLine();
              if (word.equals(line)) {
                return true;
              }
            }
            return false;
          });

    } catch (Exception e) {
      throw new RuntimeException("Problem in wordExists for word: " + word, e);
    }
  }

  private static int getIndexForToday() {
    LocalDate now = LocalDate.now();
    return now.getYear() + now.getDayOfYear() * MULTIPLY_FACTOR;
  }
}
