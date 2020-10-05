package com.tomekl007.CH02.handlers.inheritence;

public class GraphTraceRequestHandler extends BaseTraceRequestHandler<GraphTrace> {

  public GraphTraceRequestHandler(int bufferSize) {
    super(bufferSize);
  }

  @Override
  public String createPayload(GraphTrace graphTrace) {
    return graphTrace.getData() + "-nodeId";
  }
}
