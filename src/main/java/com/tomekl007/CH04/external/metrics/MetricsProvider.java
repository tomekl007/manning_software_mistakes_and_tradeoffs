package com.tomekl007.CH04.external.metrics;

public interface MetricsProvider {
  void incrementSuccess();

  void incrementFailure();

  void incrementRetry();
}
