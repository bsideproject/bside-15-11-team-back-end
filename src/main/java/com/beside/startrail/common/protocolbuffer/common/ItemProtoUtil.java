package com.beside.startrail.common.protocolbuffer.common;

import com.beside.startrail.relationship.document.Item;
import com.beside.startrail.relationship.type.ItemType;
import java.util.Objects;
import protobuf.common.ItemProto;
import protobuf.common.type.ItemTypeProto;

public class ItemProtoUtil {
  private ItemProtoUtil() {
  }

  public static ItemProto toItemProto(Item item) {
    if (Objects.isNull(item)) {
      return ItemProto
          .newBuilder()
          .clear()
          .build();
    }

    ItemProto.Builder builder = ItemProto
        .newBuilder()
        .clear();

    if (Objects.nonNull(item.getType())) {
      builder.setType(ItemTypeProto.valueOf(item.getType().name()));
    }
    if (Objects.nonNull(item.getName())) {
      builder.setName(item.getName());
    }
    if (Objects.nonNull(item.getImageLink())) {
      builder.setImageLink(item.getImageLink());
    }

    return builder
        .build();
  }

  public static Item toItem(ItemProto itemProto) {
    if (Objects.isNull(itemProto) || !itemProto.isInitialized()) {
      return null;
    }

    return Item.builder()
        .type(ItemType.valueOf(itemProto.getType().name()))
        .name(itemProto.getName())
        .imageLink(itemProto.getImageLink())
        .build();
  }
}
