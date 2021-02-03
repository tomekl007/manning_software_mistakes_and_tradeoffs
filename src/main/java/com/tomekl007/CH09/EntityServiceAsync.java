package com.tomekl007.CH09;

import java.util.concurrent.CompletableFuture;

public interface EntityServiceAsync {
  CompletableFuture<Entity> load();

  CompletableFuture<Void> save(Entity entity);
}
