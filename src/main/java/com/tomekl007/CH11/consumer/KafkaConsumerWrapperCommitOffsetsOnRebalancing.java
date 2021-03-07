package com.tomekl007.CH11.consumer;

import java.util.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaConsumerWrapperCommitOffsetsOnRebalancing implements KafkaConsumerWrapper {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(KafkaConsumerWrapperCommitOffsetsOnRebalancing.class);
  private KafkaConsumer<Integer, String> consumer;
  public List<ConsumerRecord<Integer, String>> consumedMessages = new LinkedList<>();
  // on production it should be saved to some external DB
  private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
  private final OffsetResetStrategy offsetWhenMissing;

  public KafkaConsumerWrapperCommitOffsetsOnRebalancing(
      Map<String, Object> properties, String topic, OffsetResetStrategy offsetWhenMissing) {
    consumer = new KafkaConsumer<>(properties);
    this.offsetWhenMissing = offsetWhenMissing;
    consumer.subscribe(Collections.singletonList(topic), new RebalanceListener());
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
          logic(record);
          currentOffsets.put(
              new TopicPartition(record.topic(), record.partition()),
              new OffsetAndMetadata(record.offset() + 1, "no metadata"));
        }
        consumer.commitAsync(
            currentOffsets,
            (offsets, exception) -> {
              if (exception != null) {
                LOGGER.error("problem when commitAsync", exception);
              }
            });
      }

    } catch (WakeupException e) {
      // ignore, we're closing
    } catch (Exception e) {
      LOGGER.error("Unexpected error", e);
    } finally {
      try {
        consumer.commitSync(currentOffsets);
      } finally {
        consumer.close();
      }
    }
  }

  private void logic(ConsumerRecord<Integer, String> record) {
    consumedMessages.add(record);
  }

  @Override
  public List<ConsumerRecord<Integer, String>> getConsumedEvents() {
    return consumedMessages;
  }

  private class RebalanceListener implements ConsumerRebalanceListener {

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
      LOGGER.info("onPartitions assigned: {}, offsets: {}", partitions, currentOffsets);
      partitions.forEach(
          p -> {
            OffsetAndMetadata offsetAndMetadata = currentOffsets.get(p);
            if (offsetAndMetadata == null) {
              if (offsetWhenMissing.equals(OffsetResetStrategy.EARLIEST)) {
                consumer.seekToBeginning(Collections.singletonList(p));
              } else if (offsetWhenMissing.equals(OffsetResetStrategy.LATEST)) {
                consumer.seekToEnd(Collections.singletonList(p));
              }
              return;
            }
            consumer.seek(p, offsetAndMetadata.offset());
          });
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
      LOGGER.info("Lost partitions in rebalance. Committing current offsets:" + partitions);
      consumer.commitSync(currentOffsets);
    }
  }
}
