package com.tomekl007.CH03;

public interface PersonCatalog {
  PersonInfo getPersonInfo(String personName) throws PersonCatalogException;
  boolean createPersonInfo(String personName, int amount) throws PersonCatalogException;
}
