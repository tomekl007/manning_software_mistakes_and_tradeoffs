package com.tomekl007.CH03;

import java.util.concurrent.TimeUnit;

import io.vavr.control.Try;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Fork(1)
@Warmup(iterations = 2)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ExceptionsPerformanceBenchmark {
	private static final int NUMBER_OF_ITERATIONS = 10_000;
	private static final Logger logger = LoggerFactory.getLogger(ExceptionsPerformanceBenchmark.class);

	@Benchmark
	public void baseline(Blackhole blackhole) {
		for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
			blackhole.consume(new Object());
		}
	}
	@Benchmark
	public void throwCatch(Blackhole blackhole) {
		for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
			try {
				throw new Exception();
			} catch (Exception e) {
				blackhole.consume(e);
			}
		}
	}

	@Benchmark
	public void tryMonad(Blackhole blackhole) {
		for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
			blackhole.consume(Try.of(() -> { throw new Exception();}));
		}
	}

	@Benchmark
	@Fork(value = 1, jvmArgs = "-XX:-StackTraceInThrowable")
	public void throwCatchDisableStackTrace(Blackhole blackhole) {
		for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
			try {
				throw new Exception();
			} catch (Exception e) {
				blackhole.consume(e);
			}
		}
	}

	@Benchmark
	public void getStackTrace(Blackhole blackhole) {
		for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
			try {
				throw new Exception();
			} catch (Exception e) {
				blackhole.consume(e.getStackTrace());
			}
		}
	}

	@Benchmark
	public void logError() {
		for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
			try {
				throw new Exception();
			} catch (Exception e) {
				logger.error("Error", e);
			}
		}
	}
}
