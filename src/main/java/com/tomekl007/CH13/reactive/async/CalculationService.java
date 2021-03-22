package com.tomekl007.CH13.reactive.async;

import com.tomekl007.CH13.reactive.CPUIntensiveTask;
import com.tomekl007.CH13.reactive.IOService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CalculationService {

  // non-blocking, simple threading model
  // caller decide whether to block or not
  // easy to transform between sync and async API
  // the rest of the code can be sync, and decide to get()
  public List<CompletableFuture<Integer>> calculateForUserIds(List<Integer> userIds) {
    return userIds.stream()
        .map(
            v ->
                CompletableFuture.supplyAsync(() -> IOService.blockingGet(v))
                    .thenApply(CPUIntensiveTask::calculate))
        .collect(Collectors.toList());
  }
}
