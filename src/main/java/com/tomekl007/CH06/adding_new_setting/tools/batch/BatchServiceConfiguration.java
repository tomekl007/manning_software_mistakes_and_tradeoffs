package com.tomekl007.CH06.adding_new_setting.tools.batch;

public class BatchServiceConfiguration {
  public final int batchSize;

  public BatchServiceConfiguration(int batchSize) {
    this.batchSize = batchSize;
  }

  public int getBatchSize() {
    return batchSize;
  }
}
