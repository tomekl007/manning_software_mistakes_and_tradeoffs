package com.tomekl007.CH11.consumer;

import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface KafkaConsumerWrapper {
  void startConsuming();

  List<ConsumerRecord<Integer, String>> getConsumedEvents();
}
