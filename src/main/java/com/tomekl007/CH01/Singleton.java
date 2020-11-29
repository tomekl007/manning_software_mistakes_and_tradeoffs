package com.tomekl007.CH01;

public class Singleton {

  private static Singleton instance;

  private Singleton() {}

  // WON'T work in the multi-threaded environment
  public static synchronized Singleton getInstance() {
    if (instance == null) {
      instance = new Singleton();
    }
    return instance;
  }
}
