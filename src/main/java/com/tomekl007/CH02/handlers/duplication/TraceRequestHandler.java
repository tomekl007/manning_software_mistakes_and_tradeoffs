package com.tomekl007.CH02.handlers.duplication;

import com.tomekl007.CH02.handlers.Trace;
import java.util.ArrayList;
import java.util.List;

public class TraceRequestHandler {
  private final int bufferSize;
  private boolean processed = false;
  List<String> buffer = new ArrayList<>();

  public TraceRequestHandler(int bufferSize) {
    this.bufferSize = bufferSize;
  }

  public void processRequest(Trace trace) {
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

  private String createPayload(Trace graphTrace) {
    return graphTrace.getData() + "-content";
  }

  public boolean isProcessed() {
    return processed;
  }
}
