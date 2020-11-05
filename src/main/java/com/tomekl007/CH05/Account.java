package com.tomekl007.CH05;

public class Account {
  private String name;
  private Integer id;

  public Account(String name, Integer id) {
    this.name = name;
    this.id = id;
  }

  public Account() {}

  public String getName() {
    return name;
  }

  public Integer getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
