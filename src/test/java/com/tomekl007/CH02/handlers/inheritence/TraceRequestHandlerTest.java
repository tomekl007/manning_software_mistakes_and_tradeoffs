package com.tomekl007.CH02.handlers.inheritence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TraceRequestHandlerTest {
  @Test
  public void shouldBufferTraceRequest() {
    // given
    TraceRequestHandler traceRequestHandler = new TraceRequestHandler(4);

    // when
    traceRequestHandler.processRequest(new Trace(true, "a"));
    traceRequestHandler.processRequest(new Trace(true, "b"));
    traceRequestHandler.processRequest(new Trace(true, "c"));
    traceRequestHandler.processRequest(new Trace(true, "d"));
    traceRequestHandler.processRequest(new Trace(true, "e"));

    // then
    assertThat(traceRequestHandler.buffer)
        .containsOnly("a-content", "b-content", "c-content", "d-content");
    assertThat(traceRequestHandler.isProcessed()).isTrue();
  }
}
