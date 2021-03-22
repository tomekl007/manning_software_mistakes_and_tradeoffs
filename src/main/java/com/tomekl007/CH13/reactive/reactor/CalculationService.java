package com.tomekl007.CH13.reactive.reactor;

import com.tomekl007.CH13.reactive.CPUIntensiveTask;
import com.tomekl007.CH13.reactive.IOService;
import java.util.List;
import reactor.core.publisher.Flux;

public class CalculationService {

  // new abstraction, caller needs to handle Flux
  // new threading model that we need to learn
  // the Flux and reactive API will leak to the caller,
  // potentially, we need to change the whole application to use Flux.
  public Flux<Integer> calculateForUserIds(List<Integer> userIds) {
    return Flux.fromIterable(userIds).map(IOService::blockingGet).map(CPUIntensiveTask::calculate);
  }
}
