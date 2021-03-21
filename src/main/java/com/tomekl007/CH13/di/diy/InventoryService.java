package com.tomekl007.CH13.di.diy;

public class InventoryService {

  private final InventoryDb inventoryDb;
  private final InventoryConfiguration inventoryConfiguration;

  public InventoryService(InventoryDb inventoryDb, InventoryConfiguration inventoryConfiguration) {
    this.inventoryDb = inventoryDb;
    this.inventoryConfiguration = inventoryConfiguration;
  }

  public void prepareInventory() {
    System.out.println("Preparing inventory");
  }
}
