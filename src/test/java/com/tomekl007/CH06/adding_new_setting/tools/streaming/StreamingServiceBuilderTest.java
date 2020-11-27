package com.tomekl007.CH06.adding_new_setting.tools.streaming;

import static org.assertj.core.api.Assertions.assertThat;

import com.tomekl007.CH06.adding_new_setting.client.library.CloudServiceConfiguration;
import com.tomekl007.CH06.adding_new_setting.client.library.DefaultCloudServiceClient;
import com.tomekl007.CH06.adding_new_setting.client.library.auth.UsernamePasswordAuthStrategy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class StreamingServiceBuilderTest {
  @Test
  public void shouldBuildStreamingServiceWithNewTimeoutSetting() {
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
        .isEqualTo(new CloudServiceConfiguration(new UsernamePasswordAuthStrategy("u", "p"), 1000));
  }
}
