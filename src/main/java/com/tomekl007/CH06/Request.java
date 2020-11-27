package com.tomekl007.CH06;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Request {
  @Nullable private final String token;
  @Nullable private final String username;
  @Nullable private final String password;
  private final List<String> data;

  public Request(
      @Nullable String token,
      @Nullable String username,
      @Nullable String password,
      List<String> data) {
    this.token = token;
    this.username = username;
    this.password = password;
    this.data = data;
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

  @Nonnull
  public List<String> getData() {
    return data;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Request request = (Request) o;
    return Objects.equals(token, request.token)
        && Objects.equals(username, request.username)
        && Objects.equals(password, request.password)
        && Objects.equals(data, request.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, username, password, data);
  }
}
