package com.tomekl007.CH03;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class ExceptionsPerformanceTestRunner {
	public static void main(String[] args) throws Exception {
		Options opt = new OptionsBuilder()
				.include(ExceptionsPerformanceBenchmark.class.getSimpleName())
				.build();

		new Runner(opt).run();
	}
}
