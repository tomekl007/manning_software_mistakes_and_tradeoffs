package com.tomekl007.CH06.tools.batch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.tomekl007.CH06.client.library.CloudServiceConfiguration;
import com.tomekl007.CH06.client.library.DefaultCloudServiceClient;
import com.tomekl007.CH06.client.library.auth.UsernamePasswordAuthStrategy;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class BatchServiceBuilderTest {
  @Test
  public void shouldBuildBatchServiceWithCloudConfig() {
    // given
    Path path =
        Paths.get(
            Objects.requireNonNull(
                    getClass().getClassLoader().getResource("batch-service-config.yaml"))
                .getPath());

    // when
    BatchService batchService = new BatchServiceBuilder().create(path);

    // then
    assertThat(batchService.getBatchServiceConfiguration().getBatchSize()).isEqualTo(100);
    assertThat(
            ((DefaultCloudServiceClient) batchService.getCloudServiceClient())
                .getCloudServiceConfiguration())
        .isEqualTo(new CloudServiceConfiguration(new UsernamePasswordAuthStrategy("u", "p")));
  }
}
