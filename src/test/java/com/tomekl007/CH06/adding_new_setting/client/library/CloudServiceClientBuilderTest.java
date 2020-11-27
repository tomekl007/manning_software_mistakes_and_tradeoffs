package com.tomekl007.CH06.adding_new_setting.client.library;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.tomekl007.CH06.adding_new_setting.client.library.auth.UsernamePasswordAuthStrategy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class CloudServiceClientBuilderTest {
  @Test
  public void shouldLoadCloudServiceWithTimeout() {
    // given
    Path path =
        Paths.get(
            Objects.requireNonNull(
                    getClass().getClassLoader().getResource("cloud-service-config-timeout.yaml"))
                .getPath());

    // when
    DefaultCloudServiceClient defaultCloudServiceClient =
        new CloudServiceClientBuilder().create(path);

    // then
    assertThat(defaultCloudServiceClient.getCloudServiceConfiguration().getAuthStrategy())
        .isEqualTo(new UsernamePasswordAuthStrategy("user", "pass"));
    assertThat(defaultCloudServiceClient.getCloudServiceConfiguration().getConnectionTimeout())
        .isEqualTo(1000);
  }
}
