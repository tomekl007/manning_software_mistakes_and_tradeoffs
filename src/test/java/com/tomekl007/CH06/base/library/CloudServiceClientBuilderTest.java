package com.tomekl007.CH06.base.library;

import static org.assertj.core.api.Assertions.assertThat;

import com.tomekl007.CH06.base.client.library.CloudServiceClientBuilder;
import com.tomekl007.CH06.base.client.library.DefaultCloudServiceClient;
import com.tomekl007.CH06.base.client.library.auth.TokenAuthStrategy;
import com.tomekl007.CH06.base.client.library.auth.UsernamePasswordAuthStrategy;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class CloudServiceClientBuilderTest {
  @Test
  public void shouldLoadCloudServiceWithUsernamePasswordStrategy() {
    // given
    Path path = getPath("cloud-service-config-username-password.yaml");

    // when
    DefaultCloudServiceClient defaultCloudServiceClient =
        new CloudServiceClientBuilder().create(path);

    // then
    assertThat(defaultCloudServiceClient.getCloudServiceConfiguration().getAuthStrategy())
        .isEqualTo(new UsernamePasswordAuthStrategy("user", "pass"));
  }

  @Test
  public void shouldLoadCloudServiceWithTokenStrategy() {
    // given
    Path path = getPath("cloud-service-config-token.yaml");

    // when
    DefaultCloudServiceClient defaultCloudServiceClient =
        new CloudServiceClientBuilder().create(path);

    // then
    assertThat(defaultCloudServiceClient.getCloudServiceConfiguration().getAuthStrategy())
        .isEqualTo(new TokenAuthStrategy("c8933754-30a0-11eb-adc1-0242ac120002"));
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
