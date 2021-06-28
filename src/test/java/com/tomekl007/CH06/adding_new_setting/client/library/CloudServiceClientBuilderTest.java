package com.tomekl007.CH06.adding_new_setting.client.library;

import static org.assertj.core.api.Assertions.assertThat;

import com.tomekl007.CH06.adding_new_setting.client.library.auth.UsernamePasswordAuthStrategy;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class CloudServiceClientBuilderTest {
  @Test
  public void shouldLoadCloudServiceWithTimeout() {
    // given
    Path path = getPath("cloud-service-config-timeout.yaml");

    // when
    DefaultCloudServiceClient defaultCloudServiceClient =
        new CloudServiceClientBuilder().create(path);

    // then
    assertThat(defaultCloudServiceClient.getCloudServiceConfiguration().getAuthStrategy())
        .isEqualTo(new UsernamePasswordAuthStrategy("user", "pass"));
    assertThat(defaultCloudServiceClient.getCloudServiceConfiguration().getConnectionTimeout())
        .isEqualTo(1000);
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
