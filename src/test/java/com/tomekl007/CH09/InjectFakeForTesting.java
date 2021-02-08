package com.tomekl007.CH09;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;

public class InjectFakeForTesting {
  @Test
  public void testCacheWithoutFake() throws ExecutionException, InterruptedException {
    long start = System.currentTimeMillis();
    // given
    CacheComponent cacheComponent = new CacheComponent();

    // when
    String value = cacheComponent.get("key");

    // then
    assertThat(value).isEqualTo("KEY");

    // using Thread.sleep in test is a bad pattern, see testCacheWithFake() for an improvement.
    Thread.sleep(CacheComponent.DEFAULT_EVICTION_TIME.plusSeconds(1).toMillis());
    // the eviction is done on the load operation - to trigger this we need to call the
    // get method
    assertThat(cacheComponent.get("key")).isEqualTo("KEY");
    assertThat(cacheComponent.cache.stats().evictionCount()).isEqualTo(1);

    System.out.println("end:" + (System.currentTimeMillis() - start));
  }

  @Test
  public void testCacheWithFake() throws ExecutionException {
    long start = System.currentTimeMillis();
    // given
    FakeTicker fakeTicker = new FakeTicker();
    CacheComponent cacheComponent = new CacheComponent(fakeTicker);

    // when
    String value = cacheComponent.get("key");

    // then
    assertThat(value).isEqualTo("KEY");
    fakeTicker.advance(CacheComponent.DEFAULT_EVICTION_TIME.plusSeconds(1));
    assertThat(cacheComponent.get("key")).isEqualTo("KEY");
    assertThat(cacheComponent.cache.stats().evictionCount()).isEqualTo(1);

    System.out.println("end:" + (System.currentTimeMillis() - start));
  }
}
