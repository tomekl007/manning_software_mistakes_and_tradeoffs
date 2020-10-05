package com.tomekl007.CH02.handlers.duplication;

public class Trace {
  private final boolean isTraceEnabled;
  private final String data;

  public Trace(boolean isTraceEnabled, String data) {
    this.isTraceEnabled = isTraceEnabled;
    this.data = data;
  }

  public boolean isTraceEnabled() {
    return isTraceEnabled;
  }

  public String getData() {
    return data;
  }
}
