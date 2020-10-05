package com.tomekl007.CH02.handlers.inheritence;

public abstract class TraceRequest {
  private final boolean isTraceEnabled;

  public TraceRequest(boolean isTraceEnabled) {
    this.isTraceEnabled = isTraceEnabled;
  }

  public boolean isTraceEnabled() {
    return isTraceEnabled;
  }
}
