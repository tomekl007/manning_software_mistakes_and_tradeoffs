package com.tomekl007.CH02.handlers.inheritence;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTraceRequestHandler<T extends TraceRequest> {
  private final int bufferSize;
  private boolean processed = false;
  List<String> buffer = new ArrayList<>();

  public BaseTraceRequestHandler(int bufferSize) {
    this.bufferSize = bufferSize;
  }

  public void processRequest(T trace) {
    if (!processed && !trace.isTraceEnabled()) {
      return;
    }
    if (buffer.size() < bufferSize) {
      buffer.add(createPayload(trace));
    }

    if (buffer.size() == bufferSize) {
      processed = true;
    }
  }

  protected abstract String createPayload(T graphTrace);

  public boolean isProcessed() {
    return processed;
  }
}
