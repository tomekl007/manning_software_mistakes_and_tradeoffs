package com.tomekl007.CH02.handlers.duplication;

public class GraphTrace {
  private final boolean isTraceEnabled;
  private final int data;

  public GraphTrace(boolean isTraceEnabled, int data) {
    this.isTraceEnabled = isTraceEnabled;
    this.data = data;
  }

  public boolean isTraceEnabled() {
    return isTraceEnabled;
  }

  public int getData() {
    return data;
  }
}
