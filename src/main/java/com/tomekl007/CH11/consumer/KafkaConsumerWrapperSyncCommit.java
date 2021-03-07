package com.tomekl007.CH11.consumer;

import java.util.*;
import java.util.stream.Collectors;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaConsumerWrapperSyncCommit implements KafkaConsumerWrapper {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(KafkaConsumerWrapperSyncCommit.class);
  private KafkaConsumer<Integer, String> consumer;
  public List<ConsumerRecord<Integer, String>> consumedMessages = new LinkedList<>();

  public KafkaConsumerWrapperSyncCommit(Map<String, Object> properties, String topic) {
    consumer = new KafkaConsumer<>(properties);
    consumer.subscribe(Collections.singletonList(topic));
  }

  @Override
  public void startConsuming() {
    try {
      while (true) {
        ConsumerRecords<Integer, String> records = consumer.poll(100);
        for (ConsumerRecord<Integer, String> record : records) {
          LOGGER.debug(
              "topic = {}, partition = {}, offset = {}, key = {}, value = {}",
              record.topic(),
              record.partition(),
              record.offset(),
              record.key(),
              record.value());
          logicProcessing(record); // important to be before commit offset
          try {
            consumer.commitSync();
          } catch (CommitFailedException e) {
            LOGGER.error("commit failed", e);
          }
        }
      }
    } finally {
      consumer.close();
    }
  }

  public List<TopicPartition> getPartitions() {
    return getConsumedEvents().stream()
        .map(v -> new TopicPartition(v.topic(), v.partition()))
        .collect(Collectors.toList());
  }

  public Collection<Long> getLastCommittedOffsetsPerPartitions() {
    return consumer.endOffsets(getPartitions()).values();
  }

  public Collection<Long> getLastCommittedOffsetsPerPartitions(List<TopicPartition> partitions) {
    return consumer.endOffsets(getPartitions()).values();
  }

  @Override
  public List<ConsumerRecord<Integer, String>> getConsumedEvents() {
    return consumedMessages;
  }

  private void logicProcessing(ConsumerRecord<Integer, String> record) {
    consumedMessages.add(record);
  }
}
