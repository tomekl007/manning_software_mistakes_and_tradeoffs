package com.tomekl007.CH13.di.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryDb {
  private final DbConfiguration dbConfiguration;

  @Autowired
  public InventoryDb(DbConfiguration dbConfiguration) {
    this.dbConfiguration = dbConfiguration;
  }
}
