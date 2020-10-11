package com.tomekl007.CH03;

import java.io.IOException;

public interface PublicApi {
  void check() throws IOException, InterruptedException;

  void setupService(int numberOfThreads) throws IllegalStateException, IllegalArgumentException;
}
