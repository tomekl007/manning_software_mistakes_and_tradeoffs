package com.tomekl007.CH06.deprecating_and_removing_setting.tools.batch;

import static com.tomekl007.CH06.deprecating_and_removing_setting.client.library.auth.UsernamePasswordHashedAuthStrategy.toHash;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.CloudServiceConfiguration;
import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.DefaultCloudServiceClient;
import com.tomekl007.CH06.deprecating_and_removing_setting.client.library.auth.UsernamePasswordHashedAuthStrategy;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class BatchServiceBuilderTest {
  @Test
  public void shouldThrowIfUsingNotSupportedAuthStrategy() {
    // given
    Path path = getPath("batch-service-config-timeout.yaml");

    // when
    // all batch service clients will need to migrate their yaml config to the new
    // username-password-hashed!
    // The UX of such solution is very poor. We are exposing internals of 3rd party library.
    // It means that every change of this config will need to be adapted in the clients code
    assertThatThrownBy(() -> new BatchServiceBuilder().create(path))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessageContaining("The username-password strategy is no longer supported.");
    ;
  }

  @Test
  public void shouldWorkIfUsingNotSupportedAuthStrategyButHackyWorkaroundProvided() {
    // given
    Path path = getPath("batch-service-config-timeout.yaml");

    // when
    BatchService batchService = new BatchServiceBuilderHackyWorkaround().create(path);

    // then
    assertThat(batchService.getBatchServiceConfiguration().getBatchSize()).isEqualTo(100);
    assertThat(
            ((DefaultCloudServiceClient) batchService.getCloudServiceClient())
                .getCloudServiceConfiguration())
        .isEqualTo(
            new CloudServiceConfiguration(
                new UsernamePasswordHashedAuthStrategy("u", toHash("p")), 1000));
  }

  @Test
  public void shouldWorkIfUsingHashedPasswordStrategy() {
    // given
    Path path = getPath("batch-service-config-timeout-hashed-password.yaml");

    // when
    BatchService batchService = new BatchServiceBuilder().create(path);

    // then
    assertThat(batchService.getBatchServiceConfiguration().getBatchSize()).isEqualTo(100);
    assertThat(
            ((DefaultCloudServiceClient) batchService.getCloudServiceClient())
                .getCloudServiceConfiguration())
        .isEqualTo(
            new CloudServiceConfiguration(
                new UsernamePasswordHashedAuthStrategy("u", "hashed-password"), 1000));
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
