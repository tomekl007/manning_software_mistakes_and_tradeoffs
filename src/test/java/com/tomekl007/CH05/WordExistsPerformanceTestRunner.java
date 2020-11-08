package com.tomekl007.CH05;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class WordExistsPerformanceTestRunner {
  public static void main(String[] args) throws Exception {
    Options opt =
        new OptionsBuilder().include(WordExistsPerformanceBenchmark.class.getSimpleName()).build();

    new Runner(opt).run();
  }
}
