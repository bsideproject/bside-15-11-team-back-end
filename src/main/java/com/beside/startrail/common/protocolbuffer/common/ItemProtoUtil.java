package com.beside.startrail.common.protocolbuffer.common;

import com.beside.startrail.relationship.document.Item;
import com.beside.startrail.relationship.type.ItemType;
import java.util.Objects;
import protobuf.common.ItemProto;
import protobuf.common.type.ItemTypeProto;

public class ItemProtoUtil {
  private ItemProtoUtil() {}

  public static ItemProto toItemProto(Item item) {
    if (Objects.isNull(item)) {
      return ItemProto.newBuilder().build();
    }

    return ItemProto.newBuilder()
        .setType(
            ItemTypeProto.valueOf(item.getType().name())
        )
        .setName(item.getName())
        .setImageLink(item.getImageLink())
        .build();
  }

  public static Item toItem(ItemProto itemProto) {
    if (Objects.isNull(itemProto)) {
      return null;
    }

    return Item.builder()
        .type(
            ItemType.valueOf(itemProto.getType().name())
        )
        .name(itemProto.getName())
        .imageLink(itemProto.getImageLink())
        .build();
  }
}
