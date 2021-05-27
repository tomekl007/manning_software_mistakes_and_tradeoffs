package com.tomekl007.CH01;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class PerformanceTestRunner {
  public static void main(String[] args) throws Exception {
    Options opt =
        new OptionsBuilder().include(BenchmarkSingletonVsThreadLocal.class.getSimpleName()).build();

    new Runner(opt).run();
  }
}
