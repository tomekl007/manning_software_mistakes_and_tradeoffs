package com.tomekl007.CH06.base.tools.batch;

import static org.mockito.Mockito.*;

import com.tomekl007.CH06.Request;
import com.tomekl007.CH06.base.client.library.CloudServiceClient;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

class BatchServiceTest {

  @Test
  public void shouldSendRequestIfDataExceededBatchSize() {
    // given
    BatchServiceConfiguration batchServiceConfiguration = new BatchServiceConfiguration(1);
    List<String> data = Arrays.asList("one", "two");
    CloudServiceClient cloudServiceClient = mock(CloudServiceClient.class);
    BatchService batchService = new BatchService(batchServiceConfiguration, cloudServiceClient);

    // when
    batchService.loadDataWithBatch(new Request("t", null, null, data));

    // then
    verify(cloudServiceClient, times(1)).loadData(eq(new Request("t", null, null, data)));
  }

  @Test
  public void shouldBatchUntilThereIsEnoughSpace() {
    // given
    BatchServiceConfiguration batchServiceConfiguration = new BatchServiceConfiguration(3);
    List<String> data = Arrays.asList("one", "two");
    CloudServiceClient cloudServiceClient = mock(CloudServiceClient.class);
    BatchService batchService = new BatchService(batchServiceConfiguration, cloudServiceClient);

    // when
    batchService.loadDataWithBatch(new Request("t", null, null, data));

    // then
    verify(cloudServiceClient, times(0)).loadData(eq(new Request("t", null, null, data)));

    // when 2nd data fulfill batch
    batchService.loadDataWithBatch(
        new Request("t", null, null, Collections.singletonList("three")));

    // then
    verify(cloudServiceClient, times(1))
        .loadData(eq(new Request("t", null, null, Arrays.asList("one", "two", "three"))));
  }

  @Test
  public void shouldBatchWhenExceeding() {
    // given
    BatchServiceConfiguration batchServiceConfiguration = new BatchServiceConfiguration(3);
    List<String> data = Arrays.asList("one", "two");
    CloudServiceClient cloudServiceClient = mock(CloudServiceClient.class);
    BatchService batchService = new BatchService(batchServiceConfiguration, cloudServiceClient);

    // when
    batchService.loadDataWithBatch(new Request("t", null, null, data));

    // then
    verify(cloudServiceClient, times(0)).loadData(eq(new Request("t", null, null, data)));

    // when 2nd data fulfill batch
    batchService.loadDataWithBatch(new Request("t", null, null, Arrays.asList("three", "four")));

    // then
    verify(cloudServiceClient, times(1))
        .loadData(eq(new Request("t", null, null, Arrays.asList("one", "two", "three", "four"))));
  }

  public static Stream<Arguments> dataProvider() {
    return Stream.of(Arguments.of());
  }
}
