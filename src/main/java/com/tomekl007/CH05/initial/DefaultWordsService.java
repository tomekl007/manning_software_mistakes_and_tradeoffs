package com.tomekl007.CH05.initial;

import com.google.common.annotations.VisibleForTesting;
import com.tomekl007.CH05.WordsService;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.function.IntSupplier;

public class DefaultWordsService implements WordsService {

  private static final int MULTIPLY_FACTOR = 100;
  private static final IntSupplier DEFAULT_INDEX_PROVIDER = DefaultWordsService::getIndexForToday;

  private Path filePath;

  private IntSupplier indexProvider;

  public DefaultWordsService(Path filePath) {
    this(filePath, DEFAULT_INDEX_PROVIDER);
  }

  @VisibleForTesting
  public DefaultWordsService(Path filePath, IntSupplier indexProvider) {
    this.filePath = filePath;
    this.indexProvider = indexProvider;
  }

  @Override
  public String getWordOfTheDay() {
    int index = indexProvider.getAsInt();

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
