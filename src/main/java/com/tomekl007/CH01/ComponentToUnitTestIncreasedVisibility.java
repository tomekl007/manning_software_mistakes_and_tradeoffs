package com.tomekl007.CH01;

import com.google.common.annotations.VisibleForTesting;

public class ComponentToUnitTestIncreasedVisibility {

  public int publicApiMethod() {
    return privateApiMethod();
  }

  private int privateApiMethod() {
    return complexCalculations();
  }

  @VisibleForTesting
  public int complexCalculations() {
    // some complex logic
    return 0;
  }
}
