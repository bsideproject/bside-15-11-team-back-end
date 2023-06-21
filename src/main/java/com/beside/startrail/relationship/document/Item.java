package com.beside.startrail.relationship.document;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import protobuf.common.type.ItemType;

@Builder
@Getter
@EqualsAndHashCode
public class Item {
  private final ItemType type;
  private final String name;
  private final String imageLink;
}
