package com.tomekl007.CH05.initial;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.tomekl007.CH05.Account;
import com.tomekl007.CH05.Accounts;
import com.tomekl007.CH05.SupportedAccountsLoader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class DefaultSupportedAccountsLoader implements SupportedAccountsLoader {

  private final ObjectMapper mapper;

  private final Path configFilePath;

  public DefaultSupportedAccountsLoader(Path filePath) {
    this.configFilePath = filePath;
    mapper = new ObjectMapper(new YAMLFactory());
  }

  @Override
  public List<Account> accounts() {
    try {

      return mapper.readValue(configFilePath.toFile(), Accounts.class).getAccounts();
    } catch (IOException e) {
      throw new UncheckedIOException("Problem when loading file from: " + configFilePath, e);
    }
  }

  public Optional<Account> account(Integer id) {
    return accounts().stream().filter(v -> v.getId().equals(id)).findFirst();
  }
}
