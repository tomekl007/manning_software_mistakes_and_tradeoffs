package com.tomekl007.CH04;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import java.io.IOException;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientExecution {
  private static final Logger logger = LoggerFactory.getLogger(HttpClientExecution.class);

  private final MetricRegistry metricRegistry;

  private final int numberOfRetries;

  private final CloseableHttpClient client;

  private final Meter successMeter;

  private final Meter failureMeter;

  private final Meter retryCounter;

  public HttpClientExecution(
      MetricRegistry metricRegistry, int numberOfRetries, CloseableHttpClient client) {
    this.metricRegistry = metricRegistry;
    this.successMeter = metricRegistry.meter("requests.success");
    this.failureMeter = metricRegistry.meter("requests.failure");
    this.retryCounter = metricRegistry.meter("requests.retry");
    this.numberOfRetries = numberOfRetries;
    this.client = client;
  }

  public void executeWithRetry(String path) {
    for (int i = 0; i <= numberOfRetries; i++) {
      try {
        execute(path);
        // success - return
        return;
      } catch (IOException e) {
        logger.error("Problem when sending request for retry nr: " + i, e);
        failureMeter.mark();
        if (numberOfRetries == i) {
          logger.error("This is the last retry, failing.");
          throw new RuntimeException(e);
        } else {
          logger.info("Retry once again.");
          retryCounter.mark();
        }
      }
    }
  }

  private void execute(String path) throws IOException {
    CloseableHttpResponse execute = client.execute(new HttpPost(path));
    if (execute.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      successMeter.mark();
    }
  }
}
