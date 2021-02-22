package com.tomekl007.CH10.deduplication.naive;

import com.tomekl007.CH10.deduplication.DbClient;
import com.tomekl007.CH10.deduplication.DeduplicationService;

public class NaiveDeduplicationService implements DeduplicationService {

  private final DbClient dbClient = new DbClient();

  @Override
  public boolean isDuplicate(String id) {
    boolean present = dbClient.find(id);
    if (!present) {
      dbClient.save(id);
      return false;
    } else {
      return true;
    }
  }
}
