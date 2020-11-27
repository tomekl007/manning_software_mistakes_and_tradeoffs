package com.tomekl007.CH06.client.library;

import com.tomekl007.CH06.client.library.auth.AuthStrategy;
import java.util.Objects;

public class CloudServiceConfiguration {
  private final AuthStrategy authStrategy;

  public CloudServiceConfiguration(AuthStrategy authStrategy) {
    this.authStrategy = authStrategy;
  }

  public AuthStrategy getAuthStrategy() {
    return authStrategy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CloudServiceConfiguration that = (CloudServiceConfiguration) o;
    return Objects.equals(authStrategy, that.authStrategy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authStrategy);
  }
}
