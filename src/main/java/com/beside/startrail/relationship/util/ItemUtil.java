package com.beside.startrail.relationship.util;

import com.beside.startrail.relationship.document.Item;
import java.util.Objects;
import protobuf.common.ItemDto;

public class ItemUtil {
  public static ItemDto toItemDto(Item item) {
    if (Objects.isNull(item)) {
      return ItemDto.newBuilder().build();
    }

    return ItemDto.newBuilder()
        .setType(item.getType())
        .setName(item.getName())
        .setImageLink(item.getImageLink())
        .build();
  }

  public static Item toItem(ItemDto itemDto) {
    if (Objects.isNull(itemDto)) {
      return null;
    }

    return Item.builder()
        .type(itemDto.getType())
        .name(itemDto.getName())
        .imageLink(itemDto.getImageLink())
        .build();
  }
}
