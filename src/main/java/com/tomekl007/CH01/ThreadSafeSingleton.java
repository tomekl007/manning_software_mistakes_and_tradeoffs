package com.tomekl007.CH01;

public class ThreadSafeSingleton {

  private static ThreadSafeSingleton instance;

  private ThreadSafeSingleton() {}

  public static ThreadSafeSingleton getInstance() {
    if (instance == null) {
      synchronized (ThreadSafeSingleton.class) {
        if (instance == null) {
          instance = new ThreadSafeSingleton();
        }
      }
    }
    return instance;
  }
}
