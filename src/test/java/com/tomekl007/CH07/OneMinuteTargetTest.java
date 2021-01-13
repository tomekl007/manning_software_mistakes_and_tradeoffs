package com.tomekl007.CH07;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OneMinuteTargetTest {
	@ParameterizedTest
	@ValueSource(ints = { -61, 61 })
	void outsideTargetInterval(int secondsFromTargetToClock) {
		Instant target = Instant.ofEpochSecond(10000);
		Clock clock = Clock.fixed(target.plusSeconds(secondsFromTargetToClock), ZoneOffset.UTC);
		OneMinuteTarget subject = new OneMinuteTarget(clock, target);
		assertFalse(subject.isWithinOneMinuteOfTarget());
	}
	
	@ParameterizedTest
	@ValueSource(ints = { -60, -30, 60 })
	void withinTargetInterval(int secondsFromTargetToClock) {
		Instant target = Instant.ofEpochSecond(10000);
		Clock clock = Clock.fixed(target.plusSeconds(secondsFromTargetToClock), ZoneOffset.UTC);
		OneMinuteTarget subject = new OneMinuteTarget(clock, target);
		assertTrue(subject.isWithinOneMinuteOfTarget());
	}
}
