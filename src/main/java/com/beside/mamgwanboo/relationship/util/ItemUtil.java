package com.beside.mamgwanboo.relationship.util;

import com.beside.mamgwanboo.relationship.document.Item;
import protobuf.common.ItemDto;

public class ItemUtil {
  public static ItemDto toItemDto(Item item) {
    return ItemDto.newBuilder()
        .setType(item.getType())
        .setName(item.getName())
        .setImageLink(item.getImageLink())
        .build();
  }

  public static Item toItem(ItemDto itemDto) {
    return Item.builder()
        .type(itemDto.getType())
        .name(itemDto.getName())
        .imageLink(itemDto.getImageLink())
        .build();
  }
}
