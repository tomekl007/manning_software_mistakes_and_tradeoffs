package com.tomekl007.CH01;

public class Singleton {

  private static final Singleton instance = new Singleton();

  private Singleton() {}

  // it returns the only one instance that will be shared between components
  public static Singleton getInstance() {
    return instance;
  }
}
