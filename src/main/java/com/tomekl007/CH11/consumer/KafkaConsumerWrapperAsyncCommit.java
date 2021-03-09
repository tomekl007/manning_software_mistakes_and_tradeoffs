package com.tomekl007.CH11.consumer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaConsumerWrapperAsyncCommit implements KafkaConsumerWrapper {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(KafkaConsumerWrapperAsyncCommit.class);
  private KafkaConsumer<Integer, String> consumer;
  public List<ConsumerRecord<Integer, String>> consumedMessages = new LinkedList<>();

  public KafkaConsumerWrapperAsyncCommit(Map<String, Object> properties, String topic) {
    consumer = new KafkaConsumer<>(properties);
    consumer.subscribe(Collections.singletonList(topic));
  }

  @Override
  public void startConsuming() {
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
        logicProcessing(record);
      }
      consumer.commitAsync(
          (offsets, exception) -> {
            if (exception != null) LOGGER.error("Commit failed for offsets {}", offsets, exception);
          });
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
