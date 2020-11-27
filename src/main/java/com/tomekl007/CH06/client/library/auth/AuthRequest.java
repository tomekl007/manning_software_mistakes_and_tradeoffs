package com.tomekl007.CH06.client.library.auth;

import javax.annotation.Nullable;

public class AuthRequest {
  @Nullable private final String token;
  @Nullable private final String username;
  @Nullable private final String password;

  public AuthRequest(@Nullable String token, @Nullable String username, @Nullable String password) {
    this.token = token;
    this.username = username;
    this.password = password;
  }

  @Nullable
  public String getToken() {
    return token;
  }

  @Nullable
  public String getUsername() {
    return username;
  }

  @Nullable
  public String getPassword() {
    return password;
  }
}
