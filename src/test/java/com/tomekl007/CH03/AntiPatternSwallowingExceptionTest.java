package com.tomekl007.CH03;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntiPatternSwallowingExceptionTest {
  private static final Logger logger =
      LoggerFactory.getLogger(AntiPatternSwallowingExceptionTest.class);

  @Test
  void swallowException() {
    try {
      check();
    } catch (Exception e) { // does not happen}
    }
  }

  @Test
  void swallowExceptionStderr() {
    try {
      check();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void useLoggerToPropagateExceptionInfo() {
    try {
      check();
    } catch (Exception e) {
      logger.error("Problem when check ", e);
    }
  }

  private void check() throws IOException, InterruptedException {
    throw new IOException("IO problem");
  }
}
