package com.tomekl007.CH07;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import javax.annotation.Nonnull;

public final class OneMinuteTarget {
  private static final Duration ONE_MINUTE = Duration.ofMinutes(1);
  private final Clock clock;
  private final Instant minInclusive;
  private final Instant maxInclusive;

  public OneMinuteTarget(@Nonnull Clock clock, @Nonnull Instant target) {
    this.clock = clock;
    minInclusive = target.minus(ONE_MINUTE);
    maxInclusive = target.plus(ONE_MINUTE);
  }

  public boolean isWithinOneMinuteOfTarget() {
    Instant now = clock.instant();
    return now.compareTo(minInclusive) >= 0 && now.compareTo(maxInclusive) <= 0;
  }
}
