package com.tomekl007.CH09;

import java.time.Duration;
import reactor.core.publisher.Flux;

public class ReactorCode {

  public static Flux<Integer> sumElementsWithinTimeWindow(Flux<Integer> flux) {
    return flux.window(Duration.ofSeconds(10)).flatMap(window -> window.reduce(Integer::sum));
  }
}
