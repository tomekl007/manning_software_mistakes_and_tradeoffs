package com.tomekl007.CH10.deduplication.naive;

import com.tomekl007.CH10.deduplication.DbClient;

public class NaiveDeduplicationService {

  private final DbClient dbClient = new DbClient();

  public void executeIfNotDuplicate(String id, Runnable action) {
    boolean present = dbClient.find(id);
    if (!present) {
      action.run(); // blocks for N seconds
      dbClient.save(id);
    }
  }
}
