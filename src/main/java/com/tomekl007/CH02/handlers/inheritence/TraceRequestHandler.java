package com.tomekl007.CH02.handlers.inheritence;

public class TraceRequestHandler extends BaseTraceRequestHandler<Trace> {

  public TraceRequestHandler(int bufferSize) {
    super(bufferSize);
  }

  @Override
  public String createPayload(Trace trace) {
    return trace.getData() + "-content";
  }
}
