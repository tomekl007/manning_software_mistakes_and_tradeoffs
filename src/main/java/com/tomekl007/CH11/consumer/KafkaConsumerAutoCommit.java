package com.tomekl007.CH11.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaConsumerAutoCommit implements KafkaConsumerWrapper {
  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerAutoCommit.class);
  private KafkaConsumer<Integer, String> consumer;
  public List<ConsumerRecord<Integer, String>> consumedMessages = new LinkedList<>();

  public KafkaConsumerAutoCommit(Map<String, Object> properties, String topic) {
    consumer = new KafkaConsumer<>(properties);
    consumer.subscribe(Collections.singletonList(topic));
  }

  @Override
  public void startConsuming() {
    try {
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
        }
      }
    } finally {
      consumer.close();
    }
  }

  private void logicProcessing(ConsumerRecord<Integer, String> record) {
    consumedMessages.add(record);
  }

  @Override
  public List<ConsumerRecord<Integer, String>> getConsumedEvents() {
    return consumedMessages;
  }
}
