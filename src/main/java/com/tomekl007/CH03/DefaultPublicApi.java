package com.tomekl007.CH03;

import java.io.IOException;

public class DefaultPublicApi implements PublicApi {
  boolean running;

  @Override
  public void setupService(int numberOfThreads)
      throws IllegalStateException, IllegalArgumentException {
    if (numberOfThreads < 0) {
      throw new IllegalArgumentException("Number of threads cannot be lower than 0.");
    }

    if (running) {
      throw new IllegalStateException("The service is already running.");
    }
  }

  @Override
  public void check() throws IOException, InterruptedException {}

  public void wrapIntoUnchecked() {
    try {
      check();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
