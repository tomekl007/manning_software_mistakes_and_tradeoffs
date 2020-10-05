package com.tomekl007.CH02.handlers.inheritence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GraphTraceRequestHandlerTest {
  @Test
  public void shouldBufferTraceRequest() {
    // given
    GraphTraceRequestHandler traceRequestHandler = new GraphTraceRequestHandler(4);

    // when
    traceRequestHandler.processRequest(new GraphTrace(true, 1));
    traceRequestHandler.processRequest(new GraphTrace(true, 2));
    traceRequestHandler.processRequest(new GraphTrace(true, 3));
    traceRequestHandler.processRequest(new GraphTrace(true, 4));
    traceRequestHandler.processRequest(new GraphTrace(true, 5));

    // then
    assertThat(traceRequestHandler.buffer)
        .containsOnly("1-nodeId", "2-nodeId", "3-nodeId", "4-nodeId");
    assertThat(traceRequestHandler.isProcessed()).isTrue();
  }
}
