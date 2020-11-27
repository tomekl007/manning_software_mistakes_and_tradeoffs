package com.tomekl007.CH06.base.tools.streaming;

public class StreamingServiceConfiguration {
  private final int maxTimeMs;

  public StreamingServiceConfiguration(int maxTimeMs) {
    this.maxTimeMs = maxTimeMs;
  }

  public int getMaxTimeMs() {
    return maxTimeMs;
  }
}
