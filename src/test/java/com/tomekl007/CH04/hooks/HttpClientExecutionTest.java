package com.tomekl007.CH04.hooks;

import static org.apache.http.HttpVersion.HTTP_1_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.codahale.metrics.MetricRegistry;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.junit.jupiter.api.Test;

class HttpClientExecutionTest {

  @Test
  public void shouldNotThrowEvenIfHookFailedInAUnpredictableWay() throws IOException {
    // given
    MetricRegistry metricRegistry = new MetricRegistry();
    CloseableHttpClient client = mock(CloseableHttpClient.class);
    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(response.getStatusLine())
        .thenReturn(new BasicStatusLine(HTTP_1_1, HttpStatus.SC_OK, null));
    HttpClientExecution httpClientExecution =
        new HttpClientExecution(
            metricRegistry,
            3,
            client,
            Collections.singletonList(
                new HttpRequestHook() {
                  @Override
                  public void executeOnRequest(HttpRequestBase httpRequest) {
                    throw new RuntimeException("Unpredictable problem!");
                  }
                }));

    when(client.execute(any())).thenReturn(response);

    // when
    httpClientExecution.executeWithRetry("http://localhost/user");

    // then
    assertThat(getMetric(metricRegistry, "requests.success")).isEqualTo(1);
    assertThat(getMetric(metricRegistry, "requests.failure")).isEqualTo(0);
    assertThat(getMetric(metricRegistry, "requests.retry")).isEqualTo(0);
  }

  @Test
  public void shouldParallelizeHooks() throws IOException {
    // given
    MetricRegistry metricRegistry = new MetricRegistry();
    CloseableHttpClient client = mock(CloseableHttpClient.class);
    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(response.getStatusLine())
        .thenReturn(new BasicStatusLine(HTTP_1_1, HttpStatus.SC_OK, null));
    HttpClientExecution httpClientExecution =
        new HttpClientExecution(metricRegistry, 3, client, Arrays.asList(slowHook(), slowHook()));

    long startTime = System.currentTimeMillis();
    when(client.execute(any())).thenReturn(response);

    // when
    httpClientExecution.executeWithRetry("http://localhost/user");

    long duration = System.currentTimeMillis() - startTime;

    // then
    assertThat(duration)
        .isLessThan(2000); // execution time is faster that two slow hooks on one thread
    assertThat(getMetric(metricRegistry, "requests.success")).isEqualTo(1);
    assertThat(getMetric(metricRegistry, "requests.failure")).isEqualTo(0);
    assertThat(getMetric(metricRegistry, "requests.retry")).isEqualTo(0);
  }

  private HttpRequestHook slowHook() {
    return httpRequest -> {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    };
  }

  private long getMetric(MetricRegistry metricRegistry, String metricName) {
    return metricRegistry.getMeters().entrySet().stream()
        .filter(v -> v.getKey().equals(metricName))
        .findFirst()
        .get()
        .getValue()
        .getCount();
  }
}
