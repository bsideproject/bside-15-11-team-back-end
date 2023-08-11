package com.beside.startrail.mind.document;

import com.beside.startrail.mind.type.ItemType;
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
