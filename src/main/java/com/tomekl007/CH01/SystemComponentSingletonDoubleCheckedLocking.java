package com.tomekl007.CH01;

public class SystemComponentSingletonDoubleCheckedLocking {
  private static volatile SystemComponent instance;

  private SystemComponentSingletonDoubleCheckedLocking() {}

  public static SystemComponent getInstance() {
    if (instance == null) {
      synchronized (ThreadSafeSingleton.class) {
        if (instance == null) {
          instance = new SystemComponent();
        }
      }
    }
    return instance;
  }
}
