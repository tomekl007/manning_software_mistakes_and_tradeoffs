package com.tomekl007.CH04.external.metrics;

import static org.apache.http.HttpVersion.HTTP_1_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.codahale.metrics.MetricRegistry;
import java.io.IOException;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.junit.jupiter.api.Test;

class HttpClientExecutionTest {

  @Test
  public void shouldNotRetryIfFirstRequestsSuccessful() throws IOException {
    // given
    MetricRegistry metricRegistry = new MetricRegistry();
    CloseableHttpClient client = mock(CloseableHttpClient.class);
    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(response.getStatusLine())
        .thenReturn(new BasicStatusLine(HTTP_1_1, HttpStatus.SC_OK, null));
    HttpClientExecution httpClientExecution =
        new HttpClientExecution(new DefaultMetricsProvider(metricRegistry), 3, client);

    when(client.execute(any())).thenReturn(response);

    // when
    httpClientExecution.executeWithRetry("http://localhost/user");

    // then
    assertThat(getMetric(metricRegistry, "requests.success")).isEqualTo(1);
    assertThat(getMetric(metricRegistry, "requests.failure")).isEqualTo(0);
    assertThat(getMetric(metricRegistry, "requests.retry")).isEqualTo(0);
  }

  @Test
  public void shouldRetryAndFailIfAllRequestsFailed() throws IOException {
    // given
    MetricRegistry metricRegistry = new MetricRegistry();
    CloseableHttpClient client = mock(CloseableHttpClient.class);
    when(client.execute(any())).thenThrow(new IOException("problem"));

    HttpClientExecution httpClientExecution =
        new HttpClientExecution(new DefaultMetricsProvider(metricRegistry), 3, client);

    // when
    assertThatThrownBy(
            () -> {
              httpClientExecution.executeWithRetry("url");
            })
        .hasCauseInstanceOf(IOException.class);

    // then
    assertThat(getMetric(metricRegistry, "requests.success")).isEqualTo(0);
    assertThat(getMetric(metricRegistry, "requests.failure"))
        .isEqualTo(4); // first request + nr of retries
    assertThat(getMetric(metricRegistry, "requests.retry")).isEqualTo(3);
  }

  @Test
  public void shouldRetryOnceIfTheSecondAttemptSucceeded() throws IOException {
    // given
    MetricRegistry metricRegistry = new MetricRegistry();
    CloseableHttpClient client = mock(CloseableHttpClient.class);
    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(response.getStatusLine())
        .thenReturn(new BasicStatusLine(HTTP_1_1, HttpStatus.SC_OK, null));
    when(client.execute(any())).thenThrow(new IOException("problem")).thenReturn(response);

    HttpClientExecution httpClientExecution =
        new HttpClientExecution(new DefaultMetricsProvider(metricRegistry), 3, client);

    // when
    httpClientExecution.executeWithRetry("url");

    // then first call failed and retried, second succeeded
    assertThat(getMetric(metricRegistry, "requests.success")).isEqualTo(1);
    assertThat(getMetric(metricRegistry, "requests.failure")).isEqualTo(1);
    assertThat(getMetric(metricRegistry, "requests.retry")).isEqualTo(1);
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
