package com.tomekl007.CH06.client.library.auth;

import java.util.Objects;

public class TokenAuthStrategy implements AuthStrategy {
  public TokenAuthStrategy(String token) {
    this.token = token;
  }

  private final String token;

  @Override
  public boolean authenticate(AuthRequest authRequest) {
    if (authRequest.getToken() == null) {
      return false;
    }
    return authRequest.getToken().equals(token);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TokenAuthStrategy that = (TokenAuthStrategy) o;
    return Objects.equals(token, that.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token);
  }
}
