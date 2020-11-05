package com.tomekl007.CH05;

import static org.assertj.core.api.Assertions.assertThat;

import com.tomekl007.CH05.initial.DefaultNamingService;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class NamingServiceTest {

  @ParameterizedTest
  @MethodSource("namesProvider")
  public void shouldFindIfNameIsCorrect(String name, boolean expected) {
    // given
    DefaultNamingService defaultNamingService = new DefaultNamingService();

    // when
    boolean isCorrect = defaultNamingService.checkIfNameIsCorrect(name);

    // then
    assertThat(isCorrect).isEqualTo(expected);
  }

  public static Stream<Arguments> namesProvider() {
    return Stream.of(
        Arguments.of("Tom", true),
        Arguments.of("tom", false),
        Arguments.of("#$%", false),
        Arguments.of("To", false),
        Arguments.of("T", false));
  }
}
