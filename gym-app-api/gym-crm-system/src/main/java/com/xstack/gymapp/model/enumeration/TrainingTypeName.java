package com.xstack.gymapp.model.enumeration;

public enum TrainingTypeName {
  FITNESS("Fitness"),
  YOGA("Yoga"),
  ZUMBA("Zumba"),
  STRETCHING("Stretching"),
  RESISTANCE("Resistance");

  private final String name;

  TrainingTypeName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}