package com.tomekl007.CH05.cache;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import com.google.common.base.Ticker;
import com.tomekl007.CH05.WordsService;

class CachedWordsServiceTest {
  @Test
  public void shouldGetWordOfDay() {
    // given
    Path path = getWordsPath();
    WordsService wordsService = new CachedWordsService(path, () -> 1, Ticker.systemTicker());

    // when
    String wordOfTheDay = wordsService.getWordOfTheDay();

    // then
    assertThat(wordOfTheDay).isEqualTo("aa");
  }

  @Test
  public void shouldGetLaterWordOfDay() {
    // given
    Path path = getWordsPath();
    WordsService wordsService = new CachedWordsService(path, () -> 100_000, Ticker.systemTicker());

    // when
    String wordOfTheDay = wordsService.getWordOfTheDay();

    // then
    assertThat(wordOfTheDay).isEqualTo("endostracal");
  }

  @Test
  public void shouldNotGetWordIfIndexTooHigh() {
    // given
    Path path = getWordsPath();
    WordsService wordsService =
        new CachedWordsService(path, () -> 100_000_000, Ticker.systemTicker());

    // when
    String wordOfTheDay = wordsService.getWordOfTheDay();

    // then
    assertThat(wordOfTheDay).isEqualTo("No word today.");
  }

  @Test
  public void shouldFindThatWordExists() {
    // given
    Path path = getWordsPath();
    WordsService wordsService = new CachedWordsService(path);

    // when
    boolean exists = wordsService.wordExists("make");

    // then
    assertThat(exists).isTrue();
  }

  @Test
  public void shouldFindThatWordDoesNotExists() {
    // given
    Path path = getWordsPath();
    WordsService wordsService = new CachedWordsService(path);

    // when
    boolean exists = wordsService.wordExists("make123");

    // then
    assertThat(exists).isFalse();
  }

  @Test
  public void shouldEvictContentAfterAccess() {
    // given
    FakeTicker ticker = new FakeTicker();
    Path path = getWordsPath();
    CachedWordsService wordsService = new CachedWordsService(path, ticker);

    // when
    assertThat(wordsService.wordExists("make")).isTrue();

    // then
    assertThat(wordsService.wordExistsCache.size()).isEqualTo(1);
    assertThat(wordsService.wordExistsCache.stats().missCount()).isEqualTo(1);
    assertThat(wordsService.wordExistsCache.stats().loadCount()).isEqualTo(1);
    assertThat(wordsService.wordExistsCache.stats().evictionCount()).isEqualTo(0);

    // when advance after eviction
    ticker.advance(CachedWordsService.DEFAULT_EVICTION_TIME);

    // the eviction is done on the load operation - to trigger this we need to call the
    // wordExists method
    assertThat(wordsService.wordExists("make")).isTrue();

    // then
    assertThat(wordsService.wordExistsCache.stats().evictionCount()).isEqualTo(1);
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
