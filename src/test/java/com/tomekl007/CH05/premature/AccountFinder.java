package com.tomekl007.CH05.premature;

import com.tomekl007.CH05.Account;
import java.util.List;
import java.util.Optional;

public class AccountFinder {
  private List<Account> accounts;

  public AccountFinder(List<Account> accounts) {
    this.accounts = accounts;
  }

  // we are anticipating that the stream will perform worse
  public Optional<Account> account(Integer id) {
    return accounts.stream().filter(v -> v.getId().equals(id)).findAny();
  }

  // so we are introducing multi-threading
  public Optional<Account> accountOptimized(Integer id) {
    return accounts.parallelStream().filter(v -> v.getId().equals(id)).findAny();
  }
}
