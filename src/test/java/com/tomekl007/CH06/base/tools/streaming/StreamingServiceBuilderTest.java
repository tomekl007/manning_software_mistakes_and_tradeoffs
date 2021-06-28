package com.tomekl007.CH06.base.tools.streaming;

import static org.assertj.core.api.Assertions.assertThat;

import com.tomekl007.CH06.base.client.library.CloudServiceConfiguration;
import com.tomekl007.CH06.base.client.library.DefaultCloudServiceClient;
import com.tomekl007.CH06.base.client.library.auth.UsernamePasswordAuthStrategy;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class StreamingServiceBuilderTest {
  @Test
  public void shouldBuildStreamingService() {
    // given
    Path path = getPath("streaming-service-config.yaml");

    // when
    StreamingService streamingService = new StreamingServiceBuilder().create(path);

    // then
    assertThat(streamingService.getStreamingServiceConfiguration().getMaxTimeMs()).isEqualTo(100);
    assertThat(
            ((DefaultCloudServiceClient) streamingService.getCloudServiceClient())
                .getCloudServiceConfiguration())
        .isEqualTo(new CloudServiceConfiguration(new UsernamePasswordAuthStrategy("u", "p")));
  }

  private Path getPath(String filename) {
    try {
      return Paths.get(
          Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).toURI());
    } catch (URISyntaxException e) {
      throw new IllegalStateException("Invalid " + filename + " path", e);
    }
  }
}
