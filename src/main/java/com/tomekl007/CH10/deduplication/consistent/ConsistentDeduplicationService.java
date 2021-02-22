package com.tomekl007.CH10.deduplication.consistent;

import com.tomekl007.CH10.deduplication.DbClient;
import com.tomekl007.CH10.deduplication.DeduplicationService;

public class ConsistentDeduplicationService implements DeduplicationService {

  private final DbClient dbClient = new DbClient();

  @Override
  public boolean isDuplicate(String id) {
    boolean wasInserted = dbClient.findAndInsertIfNeeded(id);
    if (wasInserted) {
      // because it was inserted, it means that it was not previously present in the db
      // return false denoting that this is not a duplicate
      return false;
    } else {
      // it was not inserted, because it was present in the db - we have our duplicate.
      return true;
    }
  }
}
