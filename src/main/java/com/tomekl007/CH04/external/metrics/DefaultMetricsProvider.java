package com.tomekl007.CH04.external.metrics;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

public class DefaultMetricsProvider implements MetricsProvider {

  private final Meter successMeter;
  private final Meter failureMeter;
  private final Meter retryCounter;

  public DefaultMetricsProvider(MetricRegistry metricRegistry) {
    this.successMeter = metricRegistry.meter("requests.success");
    this.failureMeter = metricRegistry.meter("requests.failure");
    this.retryCounter = metricRegistry.meter("requests.retry");
  }

  @Override
  public void incrementSuccess() {
    successMeter.mark();
  }

  @Override
  public void incrementFailure() {
    failureMeter.mark();
  }

  @Override
  public void incrementRetry() {
    retryCounter.mark();
  }
}
