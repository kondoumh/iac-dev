package com.example.consolexml;

public class Hoge {
  private int id;
  private String value;

  public Hoge(int id, String value) {
    this.id = id;
    this.value = value;
  }

  public Hoge() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String toString() {
    return "Hoge(id=" + this.getId() + ", value=" + this.getValue() + ")";
  }
}
