package com.xstack.gymapp.model.enumeration;

public enum TrainingTypeName {
  FITNESS("fitness"),
  YOGA("yoga"),
  ZUMBA("zumba"),
  STRETCHING("stretching"),
  RESISTANCE("resistance");

  private final String name;

  TrainingTypeName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}