package com.tomekl007.CH09;

import java.util.concurrent.*;

public class WrapIntoAsync {
  private final EntityService entityService;
  private final ExecutorService executor;

  public WrapIntoAsync(EntityService entityService) {
    this.entityService = entityService;
    executor = new ThreadPoolExecutor(1, 10, 100, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100));
  }

  public CompletableFuture<Entity> load() {
    return CompletableFuture.supplyAsync(entityService::load, executor);
  }

  public CompletableFuture<Void> save(Entity entity) {
    return CompletableFuture.runAsync(() -> entityService.save(entity), executor);
  }
}
