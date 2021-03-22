package com.tomekl007.CH13.reactive;

import java.util.Random;

public class IOService {

  private static final Random RANDOM = new Random();

  public static Integer blockingGet(Integer userId) {
    System.out.println("IOService from: " + Thread.currentThread().getName());
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return RANDOM.nextInt(userId);
  }
}
