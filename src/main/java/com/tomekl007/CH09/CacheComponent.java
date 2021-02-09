package com.tomekl007.CH09;

import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;

public class CacheComponent {
  public static final Duration DEFAULT_EVICTION_TIME = Duration.ofSeconds(5);
  public final LoadingCache<String, String> cache;

  public CacheComponent() {
    this(Ticker.systemTicker());
  }

  public CacheComponent(Ticker ticker) {
    cache =
        CacheBuilder.newBuilder()
            .expireAfterWrite(DEFAULT_EVICTION_TIME)
            .ticker(ticker)
            .recordStats()
            .build(
                new CacheLoader<String, String>() {
                  @Override
                  public String load(@Nullable String key) throws Exception {
                    return key.toUpperCase();
                  }
                });
  }

  public String get(String key) throws ExecutionException {
    return cache.get(key);
  }
}
