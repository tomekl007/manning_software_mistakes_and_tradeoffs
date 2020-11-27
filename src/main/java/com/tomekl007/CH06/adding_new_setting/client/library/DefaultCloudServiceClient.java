package com.tomekl007.CH06.adding_new_setting.client.library;

import com.tomekl007.CH06.Request;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultCloudServiceClient implements CloudServiceClient {
  private static final Logger logger = LoggerFactory.getLogger(DefaultCloudServiceClient.class);
  private CloudServiceConfiguration cloudServiceConfiguration;

  public DefaultCloudServiceClient(CloudServiceConfiguration cloudServiceConfiguration) {
    this.cloudServiceConfiguration = cloudServiceConfiguration;
  }

  @Override
  public void loadData(Request request) {
    if (cloudServiceConfiguration.getAuthStrategy().authenticate(request)) {
      insertData(request.getData());
    }
  }

  public CloudServiceConfiguration getCloudServiceConfiguration() {
    return cloudServiceConfiguration;
  }

  private void insertData(List<String> data) {
    logger.info("Inserting data: {}", data);
  }
}
