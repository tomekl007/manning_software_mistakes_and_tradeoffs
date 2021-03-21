package com.tomekl007.CH13.di.diy;

public class InventoryDb {
  private final DbConfiguration dbConfiguration;

  public InventoryDb(DbConfiguration dbConfiguration) {
    this.dbConfiguration = dbConfiguration;
  }
}
