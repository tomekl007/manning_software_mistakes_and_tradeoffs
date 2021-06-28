package com.tomekl007.CH04.listeners;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

  private final Meter successMeter;

  private final Meter failureMeter;

  private final Meter retryCounter;
  private final List<OnRetryListener> retryListeners = new ArrayList<>();

  public HttpClientExecution(
      MetricRegistry metricRegistry, int maxNumberOfRetries, CloseableHttpClient client) {
    this.successMeter = metricRegistry.meter("requests.success");
    this.failureMeter = metricRegistry.meter("requests.failure");
    this.retryCounter = metricRegistry.meter("requests.retry");
    this.maxNumberOfRetries = maxNumberOfRetries;
    this.client = client;
  }

  public void registerOnRetryListener(OnRetryListener onRetryListener) {
    retryListeners.add(onRetryListener);
  }

  public void executeWithRetry(String path) {
    List<RetryStatus> retryStatuses = new ArrayList<>();
    for (int i = 0; i <= maxNumberOfRetries; i++) {
      try {
        execute(path);
        // success - return
        return;
      } catch (IOException e) {
        logger.error("Problem when sending request for retry nr: " + i, e);
        failureMeter.mark();
        if (maxNumberOfRetries == i) {
          logger.error("This is the last retry, failing.");
          throw new RuntimeException(e);
        } else {
          retryStatuses.add(new RetryStatus(i));
          logger.info("Retry once again.");
          retryCounter.mark();
        }
      }
      // propagate the copy of an actual retryStatuses
      retryListeners.forEach(l -> l.onRetry(new ArrayList<>(retryStatuses)));

      // or fail fast and propagate a collection that does not support any modification
      //			retryListeners.forEach(l -> l.onRetry(ImmutableList.copyOf(retryStatuses)));
    }
  }

  private void execute(String path) throws IOException {
    logger.info("Executing request for: {}", path);
    CloseableHttpResponse execute = client.execute(new HttpPost(path));
    if (execute.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      successMeter.mark();
    }
  }
}
