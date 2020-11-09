package com.tomekl007.CH05.initial;

import static org.assertj.core.api.Assertions.assertThat;

import com.tomekl007.CH05.Account;
import com.tomekl007.CH05.DefaultSupportedAccountsLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class DefaultSupportedAccountsLoaderTest {
  @Test
  public void shouldGetAccountsFromYaml() {
    // given
    Path path =
        Paths.get(
            Objects.requireNonNull(getClass().getClassLoader().getResource("accounts.yaml"))
                .getPath());
    DefaultSupportedAccountsLoader defaultSupportedAccountsLoader =
        new DefaultSupportedAccountsLoader(path);

    // when
    List<Account> accounts = defaultSupportedAccountsLoader.accounts();

    // then
    assertThat(accounts.size()).isEqualTo(2);
  }
}
