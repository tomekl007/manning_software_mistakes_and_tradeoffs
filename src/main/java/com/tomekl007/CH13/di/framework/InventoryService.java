package com.tomekl007.CH13.di.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * The request scope is available in Web applications and is used here to match the discussion on
 * scopes in the book. To run this example, the scope shall be changed to singleton (default: one
 * instance for the whole application) or prototype (one instance per request to the Spring
 * ApplicationContext).
 */
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
