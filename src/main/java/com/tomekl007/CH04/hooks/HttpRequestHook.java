package com.tomekl007.CH04.hooks;

import org.apache.http.client.methods.HttpRequestBase;

public interface HttpRequestHook {
  void executeOnRequest(HttpRequestBase httpRequest);
}
