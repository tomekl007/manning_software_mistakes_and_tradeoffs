package com.tomekl007.CH05.cache;

import com.google.common.base.Ticker;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

public class FakeTicker extends Ticker {

  private final AtomicLong nanos = new AtomicLong();

  public FakeTicker advance(long nanoseconds) {
    nanos.addAndGet(nanoseconds);
    return this;
  }

  public FakeTicker advance(Duration duration) {
    return advance(duration.toNanos());
  }

  @Override
  public long read() {
    return nanos.get();
  }
}
