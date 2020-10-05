package com.tomekl007.CH02.handlers.inheritence;

public class GraphTrace extends TraceRequest {

  private final int data;

  public GraphTrace(boolean isTraceEnabled, int data) {
    super(isTraceEnabled);
    this.data = data;
  }

  public int getData() {
    return data;
  }
}
