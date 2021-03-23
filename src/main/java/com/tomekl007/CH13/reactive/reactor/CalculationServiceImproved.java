package com.tomekl007.CH13.reactive.reactor;

import com.tomekl007.CH13.reactive.CPUIntensiveTask;
import com.tomekl007.CH13.reactive.IOService;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class CalculationServiceImproved {

  // new abstraction, caller needs to handle Flux
  // new threading model that we need to learn
  // the Flux and reactive API will leak to the caller,
  // potentially, we need to change the whole application to use Flux.
  // not easy to achieve thread-affinity. Any record can be processed by any thread.
  public Flux<Integer> calculateForUserIds(List<Integer> userIds) {
    return Flux.fromIterable(userIds)
        .publishOn(Schedulers.boundedElastic())
        .map(IOService::blockingGet)
        .publishOn(Schedulers.parallel())
        .map(CPUIntensiveTask::calculate);
  }
}
