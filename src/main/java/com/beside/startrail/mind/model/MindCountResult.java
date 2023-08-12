package com.beside.startrail.mind.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MindCountResult {
  private int total;
  private int given;
  private int taken;

  public static MindCountResult makeDefault() {
    return MindCountResult
        .builder()
        .build();
  }
}
