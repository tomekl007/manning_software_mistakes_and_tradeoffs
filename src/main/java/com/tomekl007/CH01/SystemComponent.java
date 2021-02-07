package com.tomekl007.CH01;

public class SystemComponent {

  public int publicApiMethod() {
    return privateApiMethod();
  }

  private int privateApiMethod() {
    return complexCalculations();
  }

  private int complexCalculations() {
    // some complex logic
    return 0;
  }
}
