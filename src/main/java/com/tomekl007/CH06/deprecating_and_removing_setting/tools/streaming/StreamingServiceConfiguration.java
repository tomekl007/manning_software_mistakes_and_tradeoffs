package com.tomekl007.CH06.deprecating_and_removing_setting.tools.streaming;

public class StreamingServiceConfiguration {
  private final int maxTimeMs;

  public StreamingServiceConfiguration(int maxTimeMs) {
    this.maxTimeMs = maxTimeMs;
  }

  public int getMaxTimeMs() {
    return maxTimeMs;
  }
}
