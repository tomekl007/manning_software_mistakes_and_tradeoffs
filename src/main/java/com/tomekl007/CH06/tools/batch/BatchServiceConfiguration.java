package com.tomekl007.CH06.tools.batch;

public class BatchServiceConfiguration {
  public final Integer batchSize;

  public BatchServiceConfiguration(Integer batchSize) {
    this.batchSize = batchSize;
  }

  public Integer getBatchSize() {
    return batchSize;
  }
}
