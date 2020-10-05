package com.tomekl007.CH02.handlers.inheritence;

public class Trace extends TraceRequest {
  private final String data;

  public Trace(boolean isTraceEnabled, String data) {
    super(isTraceEnabled);
    this.data = data;
  }

  public String getData() {
    return data;
  }
}
