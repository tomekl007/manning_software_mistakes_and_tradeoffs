package com.tomekl007.CH09;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AsyncToSync {
  private final EntityServiceAsync entityServiceAsync;

  public AsyncToSync(EntityServiceAsync entityServiceAsync) {
    this.entityServiceAsync = entityServiceAsync;
  }

  Entity load() throws InterruptedException, ExecutionException, TimeoutException {
    return entityServiceAsync.load().get(100, TimeUnit.MILLISECONDS);
  }

  void save(Entity entity) throws InterruptedException, ExecutionException, TimeoutException {
    entityServiceAsync.load().get(100, TimeUnit.MILLISECONDS);
  }
}
