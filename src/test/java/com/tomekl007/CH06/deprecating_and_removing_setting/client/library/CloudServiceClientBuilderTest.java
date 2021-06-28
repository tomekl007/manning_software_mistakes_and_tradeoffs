package com.tomekl007.CH06.deprecating_and_removing_setting.client.library;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.auth.UsernamePasswordHashedAuthStrategy;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class CloudServiceClientBuilderTest {

  @Test
  public void shouldThrowIfUsingDeprecatedSetting() {
    // given
    Path path = getPath("cloud-service-config-username-password.yaml");

    // when
    assertThatThrownBy(() -> new CloudServiceClientBuilder().create(path))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessageContaining("The username-password strategy is no longer supported.");
  }

  @Test
  public void shouldLoadHashedUsernamePasswordStrategy() {
    // given
    Path path = getPath("cloud-service-config-username-password-hashed.yaml");

    // when
    DefaultCloudServiceClient defaultCloudServiceClient =
        new CloudServiceClientBuilder().create(path);

    // then
    assertThat(defaultCloudServiceClient.getCloudServiceConfiguration().getAuthStrategy())
        .isEqualTo(new UsernamePasswordHashedAuthStrategy("user", "hashed-password"));
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
