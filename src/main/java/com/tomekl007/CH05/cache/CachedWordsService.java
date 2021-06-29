package com.tomekl007.CH05.cache;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.tomekl007.CH05.WordsService;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public class CachedWordsService implements WordsService {

  private static final int MULTIPLY_FACTOR = 100;
  private static final Supplier<Integer> DEFAULT_INDEX_PROVIDER =
      CachedWordsService::getIndexForToday;
  public static final Duration DEFAULT_EVICTION_TIME = Duration.ofMinutes(5);
  @VisibleForTesting LoadingCache<String, Boolean> wordExistsCache;

  private Path filePath;

  private Supplier<Integer> indexProvider;

  public CachedWordsService(Path filePath) {
    this(filePath, DEFAULT_INDEX_PROVIDER, Ticker.systemTicker());
  }

  @VisibleForTesting
  public CachedWordsService(Path filePath, Ticker ticker) {
    this(filePath, DEFAULT_INDEX_PROVIDER, ticker);
  }

  @VisibleForTesting
  public CachedWordsService(Path filePath, Supplier<Integer> indexProvider, Ticker ticker) {
    this.filePath = filePath;
    this.indexProvider = indexProvider;
    this.wordExistsCache =
        CacheBuilder.newBuilder()
            .ticker(ticker)
            .expireAfterAccess(DEFAULT_EVICTION_TIME)
            .recordStats()
            .build(
                new CacheLoader<String, Boolean>() {
                  @Override
                  public Boolean load(@Nullable String word) throws Exception {
                    if (word == null) {
                      return false;
                    }
                    return checkIfWordExists(word);
                  }
                });
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
    try {
      return wordExistsCache.get(word);
    } catch (ExecutionException e) {
      throw new UncheckedExecutionException(e);
    }
  }

  private boolean checkIfWordExists(String word) {
    try (Scanner scanner = new Scanner(filePath.toFile())) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (word.equals(line)) {
          return true;
        }
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Problem in wordExists for word: " + word, e);
    }
    return false;
  }

  private static int getIndexForToday() {
    LocalDate now = LocalDate.now();
    return now.getYear() + now.getDayOfYear() * MULTIPLY_FACTOR;
  }
}
