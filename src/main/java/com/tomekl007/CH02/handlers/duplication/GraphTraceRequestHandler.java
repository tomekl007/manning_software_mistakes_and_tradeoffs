package com.tomekl007.CH02.handlers.duplication;

import com.tomekl007.CH02.handlers.GraphTrace;
import java.util.ArrayList;
import java.util.List;

public class GraphTraceRequestHandler {
  private final int bufferSize;
  private boolean processed = false;
  List<String> buffer = new ArrayList<>();

  public GraphTraceRequestHandler(int bufferSize) {
    this.bufferSize = bufferSize;
  }

  public void processRequest(GraphTrace graphTrace) {
    if (!processed && !graphTrace.isTraceEnabled()) {
      return;
    }
    if (buffer.size() < bufferSize) {
      buffer.add(createPayload(graphTrace));
    }

    if (buffer.size() >= bufferSize) {
      processed = true;
    }
  }

  private String createPayload(GraphTrace graphTrace) {
    return graphTrace.getData() + "-nodeId";
  }

  public boolean isProcessed() {
    return processed;
  }
}
