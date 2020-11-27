package com.tomekl007.CH06.deprecating_and_removing_setting.client.library.auth;

import com.tomekl007.CH06.Request;

public interface AuthStrategy {
  boolean authenticate(Request request);
}
