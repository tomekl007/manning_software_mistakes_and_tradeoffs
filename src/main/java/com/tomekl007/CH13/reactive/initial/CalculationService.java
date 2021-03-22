package com.tomekl007.CH13.reactive.initial;

import com.tomekl007.CH13.reactive.CPUIntensiveTask;
import com.tomekl007.CH13.reactive.IOService;
import java.util.List;
import java.util.stream.Collectors;

public class CalculationService {

  public List<Integer> calculateForUserIds(List<Integer> userIds) {
    return userIds.stream()
        .map(IOService::blockingGet)
        .map(CPUIntensiveTask::calculate)
        .collect(Collectors.toList());
  }
}
