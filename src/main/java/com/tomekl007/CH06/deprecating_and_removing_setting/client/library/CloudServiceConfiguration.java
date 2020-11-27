package com.tomekl007.CH06.deprecating_and_removing_setting.client.library;

import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.auth.AuthStrategy;
import java.util.Objects;

public class CloudServiceConfiguration {
  private final AuthStrategy authStrategy;
  private final Integer connectionTimeout;

  public CloudServiceConfiguration(AuthStrategy authStrategy, Integer connectionTimeout) {
    this.authStrategy = authStrategy;
    this.connectionTimeout = connectionTimeout;
  }

  public AuthStrategy getAuthStrategy() {
    return authStrategy;
  }

  public Integer getConnectionTimeout() {
    return connectionTimeout;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CloudServiceConfiguration that = (CloudServiceConfiguration) o;
    return Objects.equals(authStrategy, that.authStrategy)
        && Objects.equals(connectionTimeout, that.connectionTimeout);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authStrategy, connectionTimeout);
  }
}
