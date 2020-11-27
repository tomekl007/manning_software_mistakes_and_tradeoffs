package com.tomekl007.CH06.adding_new_setting.tools.batch;

import com.tomekl007.CH06.Request;
import com.tomekl007.CH06.adding_new_setting.client.library.CloudServiceClient;
import java.util.ArrayList;
import java.util.List;

public class BatchService {
  private final BatchServiceConfiguration batchServiceConfiguration;
  private final CloudServiceClient cloudServiceClient;
  private final List<String> batch = new ArrayList<>();

  public BatchService(
      BatchServiceConfiguration batchServiceConfiguration, CloudServiceClient cloudServiceClient) {
    this.batchServiceConfiguration = batchServiceConfiguration;
    this.cloudServiceClient = cloudServiceClient;
  }

  public CloudServiceClient getCloudServiceClient() {
    return cloudServiceClient;
  }

  public BatchServiceConfiguration getBatchServiceConfiguration() {
    return batchServiceConfiguration;
  }

  public void loadDataWithBatch(Request request) {
    batch.addAll(request.getData());

    if (batch.size() >= batchServiceConfiguration.getBatchSize()) {
      cloudServiceClient.loadData(withBatchData(request));
    }
  }

  private Request withBatchData(Request request) {
    return new Request(request.getToken(), request.getUsername(), request.getPassword(), batch);
  }
}
