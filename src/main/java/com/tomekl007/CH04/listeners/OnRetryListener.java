package com.tomekl007.CH04.listeners;

import java.util.List;

public interface OnRetryListener {
  void onRetry(List<RetryStatus> retryStatus);
}
