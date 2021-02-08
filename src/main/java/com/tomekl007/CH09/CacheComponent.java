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
            .ticker(ticker)
            .expireAfterWrite(DEFAULT_EVICTION_TIME)
            .softValues()
            .recordStats()
            .build(
                new CacheLoader<String, String>() {
                  @Override
                  public String load(@Nullable String word) throws Exception {
                    return word.toUpperCase();
                  }
                });
  }

  public String get(String key) throws ExecutionException {
    return cache.get(key);
  }
}
