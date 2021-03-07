package com.tomekl007.CH11.producer;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Sender {

  private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

  @Autowired private Producer<Integer, String> producer;

  public RecordMetadata sendBlocking(String topic, String data, Integer partitionKey) {
    LOGGER.info("sending data='{}' to topic='{}'", data, topic);
    try {
      return producer
          .send(new ProducerRecord<>(topic, partitionKey, data))
          .get(2, TimeUnit.SECONDS);
    } catch (Exception e) {
      throw new RuntimeException("problem when send", e);
    } finally {
      producer.flush();
    }
  }

  public Future<RecordMetadata> sendAsync(String topic, String data, Integer partitionKey) {
    LOGGER.info("sending data='{}' to topic='{}'", data, topic);
    try {
      return producer.send(
          new ProducerRecord<>(topic, partitionKey, data), new AsyncSenderCallback());
    } finally {
      producer.flush();
    }
  }
}
