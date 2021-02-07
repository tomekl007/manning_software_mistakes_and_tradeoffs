package com.tomekl007.CH01;

public class SystemComponentThreadLocal {
  ThreadLocal<SystemComponent> threadLocalValue = new ThreadLocal<>();

  public void set() {
    threadLocalValue.set(new SystemComponent());
  }

  public void executeAction() {
    SystemComponent systemComponent = threadLocalValue.get();
  }
}
