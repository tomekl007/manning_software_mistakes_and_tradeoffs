package com.tomekl007.CH10.deduplication;

public class DbClient {
  // finds if the entry with given id is in the db
  public boolean find(String id) {
    return true;
  }

  // saves an entry into db
  public void save(String id) {}

  // an atomic operation that tries to insert an entry with a given id;
  // If the entry is already present in the db, it returns false.
  // If the insert was performed it returns true.
  // such an operation is called upsert
  public boolean findAndInsertIfNeeded(String id) {
    return true;
  }
}
