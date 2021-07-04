package com.tomekl007.CH11.consumer;

import java.time.Duration;
import java.util.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaConsumerWrapperCommitSpecificOffsets implements KafkaConsumerWrapper {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(KafkaConsumerWrapperCommitSpecificOffsets.class);
  private KafkaConsumer<Integer, String> consumer;
  public List<ConsumerRecord<Integer, String>> consumedMessages = new LinkedList<>();
  private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

  public KafkaConsumerWrapperCommitSpecificOffsets(Map<String, Object> properties, String topic) {
    consumer = new KafkaConsumer<>(properties);
    consumer.subscribe(Collections.singletonList(topic));
  }

  @Override
  public void startConsuming() {
    int count = 0;
    while (true) {
      ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<Integer, String> record : records) {
        LOGGER.debug(
            "topic = {}, partition = {}, offset = {}, key = {}, value = {}",
            record.topic(),
            record.partition(),
            record.offset(),
            record.key(),
            record.value());
        logicProcessing(record);
        currentOffsets.put(
            new TopicPartition(record.topic(), record.partition()),
            new OffsetAndMetadata(record.offset() + 1, "no metadata"));
        if (count % 10 == 0) consumer.commitAsync(currentOffsets, null);
        count++;
      }
    }
  }

  @Override
  public List<ConsumerRecord<Integer, String>> getConsumedEvents() {
    return consumedMessages;
  }

  private void logicProcessing(ConsumerRecord<Integer, String> record) {
    consumedMessages.add(record);
  }
}
