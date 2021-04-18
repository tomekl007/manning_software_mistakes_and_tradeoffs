package com.tomekl007.CH01;

public class SystemComponentThreadLocal {
  static ThreadLocal<SystemComponent> threadLocalValue =
      ThreadLocal.withInitial(SystemComponent::new);

  public static void executeAction() {
    SystemComponent systemComponent = threadLocalValue.get();
  }

  public static SystemComponent get() {
    return threadLocalValue.get();
  }
}
