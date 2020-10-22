package com.tomekl007.CH04.hooks;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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

  private final List<HttpRequestHook> httpRequestHooks;

  private final Meter successMeter;

  private final Meter failureMeter;

  private final Meter retryCounter;

  private final ExecutorService executorService = Executors.newFixedThreadPool(8);

  public HttpClientExecution(
      MetricRegistry metricRegistry,
      int numberOfRetries,
      CloseableHttpClient client,
      List<HttpRequestHook> httpRequestHooks) {
    this.metricRegistry = metricRegistry;
    this.successMeter = metricRegistry.meter("requests.success");
    this.failureMeter = metricRegistry.meter("requests.failure");
    this.retryCounter = metricRegistry.meter("requests.retry");
    this.numberOfRetries = numberOfRetries;
    this.client = client;
    this.httpRequestHooks = httpRequestHooks;
  }

  public void executeWithRetry(String path) {
    for (int i = 0; i <= numberOfRetries; i++) {
      try {
        executeWithErrorHandlingAndParallel(path);
        // success - return
        return;
      } catch (Exception e) {
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

  private void executeWithoutErrorHandling(String path) throws IOException {
    HttpPost httpPost = new HttpPost(path);

    for (HttpRequestHook httpRequestHook : httpRequestHooks) {
      httpRequestHook.executeOnRequest(httpPost);
    }
    CloseableHttpResponse execute = client.execute(httpPost);

    if (execute.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      successMeter.mark();
    }
  }

  private void executeWithErrorHandling(String path) throws IOException {
    HttpPost httpPost = new HttpPost(path);

    for (HttpRequestHook httpRequestHook : httpRequestHooks) {
      try {
        httpRequestHook.executeOnRequest(httpPost);
      } catch (Exception ex) {
        logger.error("HttpRequestHook throws an exception. Please validate your hook logic", ex);
      }
    }
    CloseableHttpResponse execute = client.execute(httpPost);

    if (execute.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      successMeter.mark();
    }
  }

  private void executeWithErrorHandlingAndParallel(String path) throws Exception {
    HttpPost httpPost = new HttpPost(path);

    List<Callable<Object>> tasks = new ArrayList<>(httpRequestHooks.size());

    for (HttpRequestHook httpRequestHook : httpRequestHooks) {
      tasks.add(
          Executors.callable(
              () -> {
                try {
                  httpRequestHook.executeOnRequest(httpPost);
                } catch (Exception ex) {
                  logger.error(
                      "HttpRequestHook throws an exception. Please validate your hook logic", ex);
                }
              }));
    }
    List<Future<Object>> responses = executorService.invokeAll(tasks);
    for (Future<Object> response : responses) {
      response.get();
    }

    CloseableHttpResponse execute = client.execute(httpPost);

    if (execute.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
      successMeter.mark();
    }
  }
}
