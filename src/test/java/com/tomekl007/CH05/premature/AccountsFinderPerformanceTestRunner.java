package com.tomekl007.CH05.premature;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class AccountsFinderPerformanceTestRunner {
  public static void main(String[] args) throws Exception {
    Options opt =
        new OptionsBuilder()
            .include(AccountsFinderPerformanceBenchmark.class.getSimpleName())
            .build();

    new Runner(opt).run();
  }
}
