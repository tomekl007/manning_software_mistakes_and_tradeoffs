package com.tomekl007.CH06.client.library;

import com.tomekl007.CH06.client.library.auth.AuthRequest;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultCloudServiceClient implements CloudServiceClient {

  CloudServiceConfiguration cloudServiceConfiguration;

  public DefaultCloudServiceClient(CloudServiceConfiguration cloudServiceConfiguration) {
    this.cloudServiceConfiguration = cloudServiceConfiguration;
  }

  private static final Logger logger = LoggerFactory.getLogger(DefaultCloudServiceClient.class);

  @Override
  public void loadData(AuthRequest authRequest, List<String> data) {
    if (cloudServiceConfiguration.authStrategy.authenticate(authRequest)) {
      insertData(data);
    }
  }

  private void insertData(List<String> data) {
    logger.info("Inserting data: {}", data);
  }
}
