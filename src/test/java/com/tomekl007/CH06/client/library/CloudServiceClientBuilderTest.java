package com.tomekl007.CH06.client.library;

import static org.assertj.core.api.Assertions.assertThat;

import com.tomekl007.CH06.client.library.auth.TokenAuthStrategy;
import com.tomekl007.CH06.client.library.auth.UsernamePasswordAuthStrategy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class CloudServiceClientBuilderTest {
  @Test
  public void shouldLoadCloudServiceWithUsernamePasswordStrategy() {
    // given
    Path path =
        Paths.get(
            Objects.requireNonNull(
                    getClass()
                        .getClassLoader()
                        .getResource("cloud-service-config-username-password.yaml"))
                .getPath());

    // when
    DefaultCloudServiceClient defaultCloudServiceClient =
        new CloudServiceClientBuilder().create(path);

    // then
    assertThat(defaultCloudServiceClient.cloudServiceConfiguration.authStrategy)
        .isEqualTo(new UsernamePasswordAuthStrategy("user", "pass"));
  }

  @Test
  public void shouldLoadCloudServiceWithTokenStrategy() {
    // given
    Path path =
        Paths.get(
            Objects.requireNonNull(
                    getClass().getClassLoader().getResource("cloud-service-config-token.yaml"))
                .getPath());

    // when
    DefaultCloudServiceClient defaultCloudServiceClient =
        new CloudServiceClientBuilder().create(path);

    // then
    assertThat(defaultCloudServiceClient.cloudServiceConfiguration.authStrategy)
        .isEqualTo(new TokenAuthStrategy("c8933754-30a0-11eb-adc1-0242ac120002"));
  }
}
