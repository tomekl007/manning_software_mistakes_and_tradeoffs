package com.tomekl007.CH05;

import java.util.List;

public class Accounts {
  private List<Account> accounts;

  public Accounts(List<Account> accounts) {
    this.accounts = accounts;
  }

  public Accounts() {}

  public List<Account> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<Account> accounts) {
    this.accounts = accounts;
  }
}
