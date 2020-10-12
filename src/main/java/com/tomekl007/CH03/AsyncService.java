package com.tomekl007.CH03;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class AsyncService {

  public CompletableFuture<Integer> asyncExternalCall() {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            return externalCall();
          } catch (IOException e) {
            // wrapping into unchecked to propagate to the caller
            throw new RuntimeException(e);
          }
        });
  }

  public CompletableFuture<Integer> asyncExternalCallImproved() {
    CompletableFuture<Integer> result = new CompletableFuture<>();
    CompletableFuture.supplyAsync(
        () -> {
          try {
            result.complete(externalCall());
          } catch (IOException e) {
            result.completeExceptionally(e);
          }
          return null;
        });
    return result;
  }

  public int externalCall() throws IOException {
    // call to external service
    throw new IOException("Problem when calling an external service");
  }
}
