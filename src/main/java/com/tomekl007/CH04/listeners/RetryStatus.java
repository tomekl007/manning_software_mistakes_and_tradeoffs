package com.tomekl007.CH04.listeners;

public class RetryStatus {
  private final Integer retryNumber;

  public RetryStatus(Integer retryNumber) {
    this.retryNumber = retryNumber;
  }

  public Integer getRetryNumber() {
    return retryNumber;
  }
}
