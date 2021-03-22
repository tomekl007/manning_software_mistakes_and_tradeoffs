package com.tomekl007.CH13.reactive;

public class CPUIntensiveTask {

  public static Integer calculate(Integer v) {
    System.out.println("CPUIntensiveTask from: " + Thread.currentThread().getName());
    return nthFibonacciTerm(v);
  }

  private static int nthFibonacciTerm(int n) {
    if (n == 1 || n == 0) {
      return n;
    }
    return nthFibonacciTerm(n - 1) + nthFibonacciTerm(n - 2);
  }
}
