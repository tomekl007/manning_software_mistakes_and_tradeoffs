package com.tomekl007.CH01;

public class SystemComponentSingletonSynchronized {
  private static SystemComponent instance;

  private SystemComponentSingletonSynchronized() {}

  public static synchronized SystemComponent getInstance() {
    if (instance == null) {
      instance = new SystemComponent();
    }

    return instance;
  }
}
