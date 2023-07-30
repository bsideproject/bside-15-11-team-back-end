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
  // todo 이미지 기능 추가
  private final String imageLink;
}
