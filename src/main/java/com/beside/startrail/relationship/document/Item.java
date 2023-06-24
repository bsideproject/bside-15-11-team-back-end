package com.beside.startrail.relationship.document;

import com.beside.startrail.relationship.type.ItemType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class Item {
  private final ItemType type;
  private final String name;
  private final String imageLink;
}
