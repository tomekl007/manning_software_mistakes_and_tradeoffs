package com.tomekl007.CH06.base.client.library.auth;

import com.tomekl007.CH06.Request;
import java.util.Objects;

public class TokenAuthStrategy implements AuthStrategy {
  public TokenAuthStrategy(String token) {
    this.token = token;
  }

  private final String token;

  @Override
  public boolean authenticate(Request request) {
    if (request.getToken() == null) {
      return false;
    }
    return request.getToken().equals(token);
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
