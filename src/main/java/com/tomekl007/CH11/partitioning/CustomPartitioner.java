package com.tomekl007.CH11.partitioning;

import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.record.InvalidRecordException;
import org.apache.kafka.common.utils.Utils;

public class CustomPartitioner implements Partitioner {

  private static final Integer userWithDedicatedPartition = 777;

  @Override
  public void configure(Map<String, ?> configs) {}

  @Override
  public int partition(
      String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
    List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
    int numPartitions = partitions.size();
    if (numPartitions == 1) {
      return 0;
    }
    if ((keyBytes == null) || (!(key instanceof Integer)))
      throw new InvalidRecordException("We expect all messages to have Integer userId as a Key");
    if (key.equals(userWithDedicatedPartition))
      return numPartitions - 1; // that specific user will always go to the last partition
    return (Math.abs(Utils.murmur2(keyBytes)) % (numPartitions - 1));
  }

  @Override
  public void close() {}
}
