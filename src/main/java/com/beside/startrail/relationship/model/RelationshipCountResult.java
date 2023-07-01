package com.beside.startrail.relationship.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RelationshipCountResult {
  private int total;
  private int given;
  private int taken;
}
