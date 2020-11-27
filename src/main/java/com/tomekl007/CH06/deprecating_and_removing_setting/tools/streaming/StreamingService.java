package com.tomekl007.CH06.deprecating_and_removing_setting.tools.streaming;

import com.tomekl007.CH06.Request;
import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.CloudServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamingService {
  private static final Logger logger = LoggerFactory.getLogger(StreamingService.class);
  private final StreamingServiceConfiguration streamingServiceConfiguration;
  private final CloudServiceClient cloudServiceClient;

  public StreamingService(
      StreamingServiceConfiguration streamingServiceConfiguration,
      CloudServiceClient cloudServiceClient) {
    this.streamingServiceConfiguration = streamingServiceConfiguration;
    this.cloudServiceClient = cloudServiceClient;
  }

  public CloudServiceClient getCloudServiceClient() {
    return cloudServiceClient;
  }

  public StreamingServiceConfiguration getStreamingServiceConfiguration() {
    return streamingServiceConfiguration;
  }

  public void loadData(Request request) {
    long start = System.currentTimeMillis();
    cloudServiceClient.loadData(request);
    long totalTime = System.currentTimeMillis() - start;
    if (totalTime > streamingServiceConfiguration.getMaxTimeMs()) {
      logger.warn(
          "Time for streaming request exceeded! It is equal to: {}, but should be less than: {}",
          totalTime,
          streamingServiceConfiguration.getMaxTimeMs());
    }
  }
}
