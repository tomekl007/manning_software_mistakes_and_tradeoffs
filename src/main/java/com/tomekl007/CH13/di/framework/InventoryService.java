package com.tomekl007.CH13.di.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("request")
public class InventoryService {

  private final InventoryDb inventoryDb;
  private final InventoryConfiguration inventoryConfiguration;

  @Autowired
  public InventoryService(InventoryDb inventoryDb, InventoryConfiguration inventoryConfiguration) {
    this.inventoryDb = inventoryDb;
    this.inventoryConfiguration = inventoryConfiguration;
  }

  public void prepareInventory() {
    System.out.println("Preparing inventory");
  }
}
