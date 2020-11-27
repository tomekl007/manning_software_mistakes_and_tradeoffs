package com.tomekl007.CH06.client.library;

import com.tomekl007.CH06.client.library.auth.AuthRequest;
import java.util.List;

public interface CloudServiceClient {
  void loadData(AuthRequest authRequest, List<String> data);
}
