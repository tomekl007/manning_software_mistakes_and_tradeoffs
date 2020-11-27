package com.tomekl007.CH06.deprecating_and_removing_setting.client.library;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.auth.UsernamePasswordHashedAuthStrategy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class CloudServiceClientBuilderTest {

  @Test
  public void shouldThrowIfUsingDeprecatedSetting() {
    // given
    Path path =
        Paths.get(
            Objects.requireNonNull(
                    getClass()
                        .getClassLoader()
                        .getResource("cloud-service-config-username-password.yaml"))
                .getPath());

    // when
    assertThatThrownBy(() -> new CloudServiceClientBuilder().create(path))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessageContaining("The username-password strategy is no longer supported.");
  }

  @Test
  public void shouldLoadHashedUsernamePasswordStrategy() {
    // given
    Path path =
        Paths.get(
            Objects.requireNonNull(
                    getClass()
                        .getClassLoader()
                        .getResource("cloud-service-config-username-password-hashed.yaml"))
                .getPath());

    // when
    DefaultCloudServiceClient defaultCloudServiceClient =
        new CloudServiceClientBuilder().create(path);

    // then
    assertThat(defaultCloudServiceClient.getCloudServiceConfiguration().getAuthStrategy())
        .isEqualTo(new UsernamePasswordHashedAuthStrategy("user", "hashed-password"));
  }
}
