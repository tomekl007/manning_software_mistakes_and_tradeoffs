package com.tomekl007.CH06.client.library;

import com.tomekl007.CH06.client.library.auth.AuthStrategy;

public class CloudServiceConfiguration {
  public final AuthStrategy authStrategy;

  public CloudServiceConfiguration(AuthStrategy authStrategy) {
    this.authStrategy = authStrategy;
  }

  public AuthStrategy getAuthStrategy() {
    return authStrategy;
  }
}
