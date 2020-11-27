package com.tomekl007.CH06.client.library.auth;

public interface AuthStrategy {
  boolean authenticate(AuthRequest authRequest);
}
