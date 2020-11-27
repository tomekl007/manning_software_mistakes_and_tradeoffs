package com.tomekl007.CH06.deprecating_and_removing_setting.tools.streaming;

import static com.tomekl007.CH06.deprecating_and_removing_setting.client.library.auth.UsernamePasswordHashedAuthStrategy.toHash;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.CloudServiceConfiguration;
import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.DefaultCloudServiceClient;
import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.auth.UsernamePasswordHashedAuthStrategy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class StreamingServiceBuilderTest {
  // the deprecation and removal of setting did not impact our users.
  // The UX of this solution is a lot better.
  @Test
  public void shouldBuildStreamingServiceWithNewAuthStrategy() {
    // given
    Path path =
        Paths.get(
            Objects.requireNonNull(
                    getClass()
                        .getClassLoader()
                        .getResource("streaming-service-config-timeout.yaml"))
                .getPath());

    // when
    StreamingService streamingService = new StreamingServiceBuilder().create(path);

    // then
    assertThat(streamingService.getStreamingServiceConfiguration().getMaxTimeMs()).isEqualTo(100);
    assertThat(
            ((DefaultCloudServiceClient) streamingService.getCloudServiceClient())
                .getCloudServiceConfiguration())
        .isEqualTo(
            new CloudServiceConfiguration(
                new UsernamePasswordHashedAuthStrategy("u", toHash("p")), 1000));
  }
}
