package com.tomekl007.CH04.external.metrics;

import java.io.IOException;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientExecution {
  private static final Logger logger = LoggerFactory.getLogger(HttpClientExecution.class);
  private final int maxNumberOfRetries;
  private final CloseableHttpClient client;

  private MetricsProvider metricsProvider;

  public HttpClientExecution(
      MetricsProvider metricsProvider, int maxNumberOfRetries, CloseableHttpClient client) {
    this.metricsProvider = metricsProvider;
    this.maxNumberOfRetries = maxNumberOfRetries;
    this.client = client;
  }

  public void executeWithRetry(String path) {
    for (int i = 0; i <= maxNumberOfRetries; i++) {
      try {
        execute(path);
        // success - return
        return;
      } catch (IOException e) {
        logger.error("Problem when sending request for retry nr: " + i, e);
        metricsProvider.incrementFailure();
        if (maxNumberOfRetries == i) {
          logger.error("This is the last retry, failing.");
          throw new RuntimeException(e);
        } else {
          logger.info("Retry once again.");
          metricsProvider.incrementRetry();
        }
      }
    }
  }

  private void execute(String path) throws IOException {
    CloseableHttpResponse execute = client.execute(new HttpPost(path));
    if (execute.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      metricsProvider.incrementSuccess();
    }
  }
}
