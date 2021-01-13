package com.tomekl007.CH07.untestable;

import java.time.Duration;
import java.time.Instant;

public final class OneMinuteTarget {
    private static final Duration ONE_MINUTE = Duration.ofMinutes(1);
    private final Instant minInclusive;
    private final Instant maxInclusive;

    public OneMinuteTarget(Instant target) {
        minInclusive = target.minus(ONE_MINUTE);
        maxInclusive = target.plus(ONE_MINUTE);
    }

    public boolean isWithinOneMinuteOfTarget() {
        Instant now = Instant.now();
        return now.compareTo(minInclusive) >= 0 &&
            now.compareTo(maxInclusive) <= 0;
    }
}
