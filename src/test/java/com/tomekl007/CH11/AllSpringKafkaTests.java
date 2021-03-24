package com.tomekl007.CH11;

import com.tomekl007.CH11.consumer.SpringKafkaReceiverTest;
import com.tomekl007.CH11.producer.SpringKafkaSenderTest;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;

@RunWith(Suite.class)
@SuiteClasses({SpringKafkaSenderTest.class, SpringKafkaReceiverTest.class})
public class AllSpringKafkaTests {

  private static final Logger LOGGER = LoggerFactory.getLogger(AllSpringKafkaTests.class);

  public static final String SENDER_TOPIC = "sender.t";
  public static final String RECEIVER_TOPIC = "receiver.t";
  public static final String CONSUMER_TEST_TOPIC = "consumer_test_topic";
  public static final String CONSUMER_TEST_TOPIC_COMMIT_SYNC = "consumer_test_topic_commit_sync";
  public static final String CONSUMER_TEST_TOPIC_COMMIT_ASYNC = "consumer_test_topic_commit_async";
  public static final String CONSUMER_TEST_TOPIC_COMMIT_ASYNC_AND_SYNC =
      "consumer_test_topic_commit_async_and_sync";
  public static final String CONSUMER_TEST_TOPIC_COMMIT_SPECIFIC_OFFSETS =
      "consumer_test_topic_commit_specific_offsets";
  public static final Integer NUMBER_OF_PARTITIONS_PER_TOPIC = 4;

  @ClassRule
  public static EmbeddedKafkaRule embeddedKafka =
      new EmbeddedKafkaRule(
          1,
          true,
          NUMBER_OF_PARTITIONS_PER_TOPIC,
          SENDER_TOPIC,
          RECEIVER_TOPIC,
          CONSUMER_TEST_TOPIC,
          CONSUMER_TEST_TOPIC_COMMIT_SYNC,
          CONSUMER_TEST_TOPIC_COMMIT_ASYNC,
          CONSUMER_TEST_TOPIC_COMMIT_ASYNC_AND_SYNC,
          CONSUMER_TEST_TOPIC_COMMIT_SPECIFIC_OFFSETS);

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    String kafkaBootstrapServers = embeddedKafka.getEmbeddedKafka().getBrokersAsString();

    LOGGER.debug("kafkaServers='{}'", kafkaBootstrapServers);
    // override the property in application.properties
    System.setProperty("kafka.bootstrap-servers", kafkaBootstrapServers);
  }
}
