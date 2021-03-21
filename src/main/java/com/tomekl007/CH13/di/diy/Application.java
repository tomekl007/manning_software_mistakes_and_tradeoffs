package com.tomekl007.CH13.di.diy;

import org.jetbrains.annotations.NotNull;

public class Application {

  public static void main(String[] args) {
    // construct dependencies
    DbConfiguration dbConfiguration = loadDbConfig();
    InventoryConfiguration inventoryConfiguration = loadInventoryConfig();
    InventoryDb inventoryDb = new SpecializedInventoryDb(dbConfiguration);
    InventoryService inventoryService = new InventoryService(inventoryDb, inventoryConfiguration);
    inventoryService.prepareInventory();
  }

  @NotNull
  private static InventoryConfiguration loadInventoryConfig() {
    return new InventoryConfiguration();
  }

  @NotNull
  private static DbConfiguration loadDbConfig() {
    return new DbConfiguration();
  }
}
