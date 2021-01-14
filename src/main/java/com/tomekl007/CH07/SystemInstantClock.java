package com.tomekl007.CH07;

import java.time.Instant;

public final class SystemInstantClock implements InstantClock {
  private static final SystemInstantClock instance = new SystemInstantClock();

  // Prevent instantiation elsewhere.
  private SystemInstantClock() {}

  // Public method to access the singleton instance.
  public static SystemInstantClock getInstance() {
    return instance;
  }

  public Instant getCurrentInstant() {
    // Delegate to Instant.now() which uses the system clock.
    return Instant.now();
  }
}
