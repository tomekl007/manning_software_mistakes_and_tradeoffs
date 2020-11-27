package com.tomekl007.CH06.deprecating_and_removing_setting.tools.batch;

public class BatchServiceConfiguration {
  public final Integer batchSize;

  public BatchServiceConfiguration(Integer batchSize) {
    this.batchSize = batchSize;
  }

  public Integer getBatchSize() {
    return batchSize;
  }
}
