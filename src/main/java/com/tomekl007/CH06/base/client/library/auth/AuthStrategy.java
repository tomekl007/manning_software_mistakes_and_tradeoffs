package com.tomekl007.CH06.base.client.library.auth;

import com.tomekl007.CH06.Request;

public interface AuthStrategy {
  boolean authenticate(Request request);
}
