package com.tomekl007.CH05.initial;

import static org.assertj.core.api.Assertions.assertThat;

import com.tomekl007.CH05.WordsService;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class DefaultWordsServiceTest {
  @Test
  public void shouldGetWordOfDay() {
    // given
    Path path = getWordsPath();
    WordsService wordsService = new DefaultWordsService(path, () -> 1);

    // when
    String wordOfTheDay = wordsService.getWordOfTheDay();

    // then
    assertThat(wordOfTheDay).isEqualTo("aa");
  }

  @Test
  public void shouldGetLaterWordOfDay() {
    // given
    Path path = getWordsPath();
    WordsService wordsService = new DefaultWordsService(path, () -> 100_000);

    // when
    String wordOfTheDay = wordsService.getWordOfTheDay();

    // then
    assertThat(wordOfTheDay).isEqualTo("endostracal");
  }

  @Test
  public void shouldNotGetWordIfIndexTooHigh() {
    // given
    Path path = getWordsPath();
    WordsService wordsService = new DefaultWordsService(path, () -> 100_000_000);

    // when
    String wordOfTheDay = wordsService.getWordOfTheDay();

    // then
    assertThat(wordOfTheDay).isEqualTo("No word today.");
  }

  @Test
  public void shouldFindThatWordExists() {
    // given
    Path path = getWordsPath();
    WordsService wordsService = new DefaultWordsService(path);

    // when
    boolean exists = wordsService.wordExists("make");

    // then
    assertThat(exists).isTrue();
  }

  @Test
  public void shouldFindThatWordDoesNotExists() {
    // given
    Path path = getWordsPath();
    WordsService wordsService = new DefaultWordsService(path);

    // when
    boolean exists = wordsService.wordExists("make123");

    // then
    assertThat(exists).isFalse();
  }

  private Path getWordsPath() {
    return Paths.get(
        Objects.requireNonNull(getClass().getClassLoader().getResource("words.txt")).getPath());
  }
}
