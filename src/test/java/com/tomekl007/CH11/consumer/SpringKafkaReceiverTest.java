package com.tomekl007.CH11.consumer;

import static com.tomekl007.CH11.AllSpringKafkaTests.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tomekl007.CH11.AllSpringKafkaTests;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringKafkaReceiverTest {
  @Autowired private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

  private Producer<Integer, String> kafkaProducer;

  @Before
  public void setUp() throws Exception {
    // set up the Kafka producer properties
    Map<String, Object> senderProperties =
        KafkaTestUtils.producerProps(AllSpringKafkaTests.embeddedKafka.getEmbeddedKafka());

    // create a Kafka producer factory
    ProducerFactory<Integer, String> producerFactory =
        new DefaultKafkaProducerFactory<>(senderProperties);

    kafkaProducer = producerFactory.createProducer();

    // wait until the partitions are assigned
    for (MessageListenerContainer messageListenerContainer :
        kafkaListenerEndpointRegistry.getListenerContainers()) {
      ContainerTestUtils.waitForAssignment(
          messageListenerContainer,
          AllSpringKafkaTests.embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
    }
  }

  @Test
  public void givenConsumerWithSyncCommit_whenSendMessageToIt_thenShouldReceiveInThePoolLoop()
      throws Exception {
    // given
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    String message = "Send unique message " + UUID.randomUUID().toString();
    String gropupId = "group_id" + UUID.randomUUID().toString();

    KafkaConsumerWrapperSyncCommit kafkaConsumer =
        new KafkaConsumerWrapperSyncCommit(
            consumerConfigs(
                gropupId,
                "false",
                AllSpringKafkaTests.embeddedKafka.getEmbeddedKafka().getBrokersAsString()),
            CONSUMER_TEST_TOPIC_COMMIT_SYNC);

    // when
    executorService.submit(kafkaConsumer::startConsuming);

    for (int i = 0; i < 10; i++) {
      kafkaProducer
          .send(new ProducerRecord<>(CONSUMER_TEST_TOPIC_COMMIT_SYNC, message))
          .get(100, TimeUnit.SECONDS);
    }

    // then
    executorService.awaitTermination(4, TimeUnit.SECONDS);
    executorService.shutdown();
    assertThat(kafkaConsumer.getConsumedEvents().size()).isLessThanOrEqualTo(10);
    assertThat(kafkaConsumer.getConsumedEvents().get(0).value()).isEqualTo(message);

    // when
    String messageNewProducer = "Send unique message " + UUID.randomUUID().toString();
    KafkaConsumerWrapperSyncCommit newConsumer =
        new KafkaConsumerWrapperSyncCommit(
            consumerConfigs(
                gropupId,
                "false",
                AllSpringKafkaTests.embeddedKafka.getEmbeddedKafka().getBrokersAsString()),
            CONSUMER_TEST_TOPIC_COMMIT_SYNC);

    ExecutorService executorService2 = Executors.newSingleThreadExecutor();
    executorService2.submit(newConsumer::startConsuming);

    for (int i = 10; i < 20; i++) {
      kafkaProducer
          .send(new ProducerRecord<>(CONSUMER_TEST_TOPIC_COMMIT_SYNC, messageNewProducer))
          .get(100, TimeUnit.SECONDS);
    }

    executorService2.awaitTermination(4, TimeUnit.SECONDS);
    executorService2.shutdown();

    assertThat(newConsumer.getConsumedEvents().size()).isLessThanOrEqualTo(10);
    Assertions.assertThat(hasDuplicates(message, newConsumer)).isFalse();
  }

  @Test
  public void givenConsumerWithASyncCommit_whenSendMessageToIt_thenShouldReceiveInThePoolLoop()
      throws Exception {
    // given
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    String message = "Send unique message " + UUID.randomUUID().toString();
    String gropupId = "group_id" + UUID.randomUUID().toString();

    KafkaConsumerWrapperAsyncCommit kafkaConsumer =
        new KafkaConsumerWrapperAsyncCommit(
            consumerConfigs(
                gropupId,
                "false",
                AllSpringKafkaTests.embeddedKafka.getEmbeddedKafka().getBrokersAsString()),
            CONSUMER_TEST_TOPIC_COMMIT_ASYNC);

    // when
    executorService.submit(kafkaConsumer::startConsuming);

    for (int i = 0; i < 10; i++) {
      kafkaProducer
          .send(new ProducerRecord<>(CONSUMER_TEST_TOPIC_COMMIT_ASYNC, message))
          .get(100, TimeUnit.SECONDS);
    }

    // then
    executorService.awaitTermination(4, TimeUnit.SECONDS);
    executorService.shutdown();
    assertThat(kafkaConsumer.getConsumedEvents().size()).isLessThanOrEqualTo(10);
    assertThat(kafkaConsumer.getConsumedEvents().get(0).value()).isEqualTo(message);

    // when
    String messageNewProducer = "Send unique message " + UUID.randomUUID().toString();
    KafkaConsumerWrapperAsyncCommit newConsumer =
        new KafkaConsumerWrapperAsyncCommit(
            consumerConfigs(
                gropupId,
                "false",
                AllSpringKafkaTests.embeddedKafka.getEmbeddedKafka().getBrokersAsString()),
            CONSUMER_TEST_TOPIC_COMMIT_ASYNC);

    ExecutorService executorService2 = Executors.newSingleThreadExecutor();
    executorService2.submit(newConsumer::startConsuming);

    for (int i = 10; i < 20; i++) {
      kafkaProducer
          .send(new ProducerRecord<>(CONSUMER_TEST_TOPIC_COMMIT_ASYNC, messageNewProducer))
          .get(100, TimeUnit.SECONDS);
    }

    executorService2.awaitTermination(4, TimeUnit.SECONDS);
    executorService2.shutdown();

    assertThat(newConsumer.getConsumedEvents().size()).isLessThanOrEqualTo(10);
    Assertions.assertThat(hasDuplicates(message, newConsumer)).isFalse();
  }

  @Test
  public void
      givenConsumerWithASyncAndSyncCommit_whenSendMessageToIt_thenShouldReceiveInThePoolLoop()
          throws Exception {
    // given
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    String message = "Send unique message " + UUID.randomUUID().toString();
    String gropupId = "group_id" + UUID.randomUUID().toString();

    KafkaConsumerWrapper kafkaConsumer =
        new KafkaConsumerWrapperAsyncCommitAndOnShutdown(
            consumerConfigs(
                gropupId,
                "false",
                AllSpringKafkaTests.embeddedKafka.getEmbeddedKafka().getBrokersAsString()),
            CONSUMER_TEST_TOPIC_COMMIT_ASYNC_AND_SYNC);

    // when
    executorService.submit(kafkaConsumer::startConsuming);

    for (int i = 0; i < 10; i++) {
      kafkaProducer
          .send(new ProducerRecord<>(CONSUMER_TEST_TOPIC_COMMIT_ASYNC_AND_SYNC, message))
          .get(100, TimeUnit.SECONDS);
    }

    // then
    executorService.awaitTermination(4, TimeUnit.SECONDS);
    executorService.shutdown();
    assertThat(kafkaConsumer.getConsumedEvents().size()).isLessThanOrEqualTo(10);
    assertThat(kafkaConsumer.getConsumedEvents().get(0).value()).isEqualTo(message);

    // when
    String messageNewProducer = "Send unique message " + UUID.randomUUID().toString();
    KafkaConsumerWrapper newConsumer =
        new KafkaConsumerWrapperAsyncCommitAndOnShutdown(
            consumerConfigs(
                gropupId,
                "false",
                AllSpringKafkaTests.embeddedKafka.getEmbeddedKafka().getBrokersAsString()),
            CONSUMER_TEST_TOPIC_COMMIT_ASYNC_AND_SYNC);

    ExecutorService executorService2 = Executors.newSingleThreadExecutor();
    executorService2.submit(newConsumer::startConsuming);

    for (int i = 10; i < 20; i++) {
      kafkaProducer
          .send(new ProducerRecord<>(CONSUMER_TEST_TOPIC_COMMIT_ASYNC_AND_SYNC, messageNewProducer))
          .get(100, TimeUnit.SECONDS);
    }

    executorService2.awaitTermination(4, TimeUnit.SECONDS);
    executorService2.shutdown();

    assertThat(newConsumer.getConsumedEvents().size()).isLessThanOrEqualTo(10);
    assertThat(hasDuplicates(message, newConsumer)).isFalse();
  }

  @Test
  public void
      givenTwoConsumersWithDifferentGroupIds_whenSendMessageToTopic_thenBothShouldReceiveMessages()
          throws InterruptedException, ExecutionException, TimeoutException {
    // given
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    KafkaConsumerWrapper kafkaConsumerFirst =
        new KafkaConsumerWrapperSyncCommit(
            KafkaTestUtils.consumerProps(
                "group_id" + UUID.randomUUID().toString(),
                "false",
                AllSpringKafkaTests.embeddedKafka.getEmbeddedKafka()),
            CONSUMER_TEST_TOPIC);
    KafkaConsumerWrapper kafkaConsumerSecond =
        new KafkaConsumerWrapperSyncCommit(
            KafkaTestUtils.consumerProps(
                "group_id" + UUID.randomUUID().toString(),
                "false",
                AllSpringKafkaTests.embeddedKafka.getEmbeddedKafka()),
            CONSUMER_TEST_TOPIC);

    // when
    executorService.submit(kafkaConsumerFirst::startConsuming);
    executorService.submit(kafkaConsumerSecond::startConsuming);

    for (int i = 0; i < 10; i++) {
      kafkaProducer
          .send(
              new ProducerRecord<>(
                  CONSUMER_TEST_TOPIC, "Send unique message " + UUID.randomUUID().toString()))
          .get(1, TimeUnit.SECONDS);
    }

    // then
    executorService.awaitTermination(4, TimeUnit.SECONDS);
    executorService.shutdown();
    Assertions.assertThat(
            hasDuplicates(
                kafkaConsumerFirst.getConsumedEvents(), kafkaConsumerSecond.getConsumedEvents()))
        .isTrue();
  }

  @Test
  public void
      givenConsumerThatCommitsSpecificOffsets_whenSendMessageToIt_thenShouldReceiveInThePoolLoop()
          throws Exception {
    // given
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    String message = "Send unique message " + UUID.randomUUID().toString();
    String gropupId = "group_id" + UUID.randomUUID().toString();

    KafkaConsumerWrapper kafkaConsumer =
        new KafkaConsumerWrapperCommitSpecificOffsets(
            consumerConfigs(
                gropupId,
                "false",
                AllSpringKafkaTests.embeddedKafka.getEmbeddedKafka().getBrokersAsString()),
            CONSUMER_TEST_TOPIC_COMMIT_SPECIFIC_OFFSETS);

    // when
    executorService.submit(kafkaConsumer::startConsuming);

    for (int i = 0; i < 100; i++) {
      kafkaProducer
          .send(new ProducerRecord<>(CONSUMER_TEST_TOPIC_COMMIT_SPECIFIC_OFFSETS, message))
          .get(100, TimeUnit.SECONDS);
    }

    // then
    executorService.awaitTermination(4, TimeUnit.SECONDS);
    executorService.shutdown();
    assertThat(kafkaConsumer.getConsumedEvents().size()).isLessThanOrEqualTo(100);
    assertThat(kafkaConsumer.getConsumedEvents().get(0).value()).isEqualTo(message);

    // when
    String messageNewProducer = "Send unique message " + UUID.randomUUID().toString();
    KafkaConsumerWrapper newConsumer =
        new KafkaConsumerWrapperCommitSpecificOffsets(
            consumerConfigs(
                gropupId,
                "false",
                AllSpringKafkaTests.embeddedKafka.getEmbeddedKafka().getBrokersAsString()),
            CONSUMER_TEST_TOPIC_COMMIT_SPECIFIC_OFFSETS);

    ExecutorService executorService2 = Executors.newSingleThreadExecutor();
    executorService2.submit(newConsumer::startConsuming);

    for (int i = 100; i < 200; i++) {
      kafkaProducer
          .send(
              new ProducerRecord<>(CONSUMER_TEST_TOPIC_COMMIT_SPECIFIC_OFFSETS, messageNewProducer))
          .get(100, TimeUnit.SECONDS);
    }

    executorService2.awaitTermination(4, TimeUnit.SECONDS);
    executorService2.shutdown();

    assertThat(newConsumer.getConsumedEvents().size()).isLessThanOrEqualTo(100);
    assertThat(hasDuplicates(message, newConsumer)).isFalse();
  }

  @Test
  @Ignore
  public void
      givenConsumer_whenSendMessageToItAndOffsetOnRebalancingIsLargest_thenShouldConsumeOnlyRecentMessages()
          throws Exception {
    // given
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    String message = "Send unique message " + UUID.randomUUID().toString();

    KafkaConsumerWrapper kafkaConsumer =
        new KafkaConsumerWrapperCommitOffsetsOnRebalancing(
            KafkaTestUtils.consumerProps(
                "group_id" + UUID.randomUUID().toString(),
                "false",
                AllSpringKafkaTests.embeddedKafka.getEmbeddedKafka()),
            CONSUMER_TEST_TOPIC,
            OffsetResetStrategy.LATEST); // latest is default in kafka

    // when
    sendTenMessages(message);

    executorService.submit(kafkaConsumer::startConsuming);

    sendTenMessages(message);

    // then
    executorService.awaitTermination(4, TimeUnit.SECONDS);
    executorService.shutdown();
    assertThat(kafkaConsumer.getConsumedEvents().size()).isLessThanOrEqualTo(10);
    assertThat(kafkaConsumer.getConsumedEvents().get(0).value()).isEqualTo(message);
  }

  private void sendTenMessages(String message)
      throws InterruptedException, ExecutionException, TimeoutException {
    for (int i = 0; i < 10; i++) {
      kafkaProducer
          .send(new ProducerRecord<>(CONSUMER_TEST_TOPIC, message))
          .get(100, TimeUnit.SECONDS);
    }
  }

  @Test
  public void
      givenConsumer_whenSendMessageToItAndOffsetOnRebalancingIsEarliest_thenShouldConsumeMessagesFromTheBeginning()
          throws Exception {
    // given
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    String message = "Send unique message " + UUID.randomUUID().toString();

    KafkaConsumerWrapper kafkaConsumer =
        new KafkaConsumerWrapperCommitOffsetsOnRebalancing(
            KafkaTestUtils.consumerProps(
                "group_id" + UUID.randomUUID().toString(),
                "false",
                AllSpringKafkaTests.embeddedKafka.getEmbeddedKafka()),
            CONSUMER_TEST_TOPIC,
            OffsetResetStrategy.EARLIEST);

    // when
    sendTenMessages(message);
    executorService.submit(kafkaConsumer::startConsuming);
    sendTenMessages(message);

    // then
    executorService.awaitTermination(4, TimeUnit.SECONDS);
    executorService.shutdown();
    assertThat(kafkaConsumer.getConsumedEvents().size()).isGreaterThanOrEqualTo(20);
  }

  public Map<String, Object> consumerConfigs(String groupId, String autoCommit, String brokers) {
    Map<String, Object> props = new HashMap<>();

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    return props;
  }

  private boolean hasDuplicates(String duplicate, KafkaConsumerWrapper newConsumer) {
    return newConsumer.getConsumedEvents().stream()
        .map(ConsumerRecord::value)
        .collect(Collectors.toList())
        .contains(duplicate);
  }

  private boolean hasDuplicates(
      List<ConsumerRecord<Integer, String>> first, List<ConsumerRecord<Integer, String>> second) {
    Set<String> setSecond = second.stream().map(ConsumerRecord::value).collect(Collectors.toSet());
    return first.stream().map(ConsumerRecord::value).anyMatch(setSecond::contains);
  }
}
