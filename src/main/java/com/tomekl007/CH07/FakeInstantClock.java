package com.tomekl007.CH07;

import java.time.Instant;
import javax.annotation.Nonnull;

public final class FakeInstantClock implements InstantClock {
  private final Instant currentInstant;

  public FakeInstantClock(@Nonnull Instant currentInstant) {
    this.currentInstant = currentInstant;
  }

  public Instant getCurrentInstant() {
    return currentInstant;
  }
}
