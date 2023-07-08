package com.beside.startrail.user.type;

import java.time.LocalDate;

public enum AgeRangeType {
  UNDER_TEEN(0),
  TEENS(10),
  TWENTIES(20),
  THIRTIES(30),
  FORTIES(40),
  FIFTIES(50),
  OVER_FIFTY(60);

  private final int age;

  AgeRangeType(int age) {
    this.age = age;
  }


  public int getDefaultBirthYear() {
    return LocalDate.now().getYear() - this.age;
  }
}
