package com.tomekl007.CH06.deprecating_and_removing_setting.client.library.auth;

import com.google.common.hash.Hashing;
import com.tomekl007.CH06.Request;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class UsernamePasswordHashedAuthStrategy implements AuthStrategy {
  private final String username;
  private final String passwordHash;

  public UsernamePasswordHashedAuthStrategy(String username, String passwordHash) {
    this.username = username;
    this.passwordHash = passwordHash;
  }

  @Override
  public boolean authenticate(Request request) {
    if (request.getUsername() == null || request.getPassword() == null) {
      return false;
    }

    return request.getUsername().equals(username)
        && toHash(request.getPassword()).equals(passwordHash);
  }

  @SuppressWarnings("UnstableApiUsage")
  public static String toHash(String password) {

    return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UsernamePasswordHashedAuthStrategy that = (UsernamePasswordHashedAuthStrategy) o;
    return Objects.equals(username, that.username)
        && Objects.equals(passwordHash, that.passwordHash);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, passwordHash);
  }

  @Override
  public String toString() {
    return "UsernamePasswordHashedAuthStrategy{"
        + "username='"
        + username
        + '\''
        + ", passwordHash='"
        + passwordHash
        + '\''
        + '}';
  }
}
