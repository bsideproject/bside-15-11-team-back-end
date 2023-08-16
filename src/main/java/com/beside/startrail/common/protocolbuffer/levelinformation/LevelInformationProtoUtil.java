package com.beside.startrail.common.protocolbuffer.levelinformation;

import com.beside.startrail.relationshiplevel.document.RelationshipLevel;
import java.util.Objects;
import protobuf.common.LevelInformationProto;

public class LevelInformationProtoUtil {
  private LevelInformationProtoUtil() {
  }

  public static LevelInformationProto toLevelInformationProto(
      RelationshipLevel relationshipLevel,
      int total,
      int given,
      int taken
  ) {
    if (Objects.isNull(relationshipLevel)) {
      return LevelInformationProto
          .newBuilder()
          .clear()
          .build();
    }

    LevelInformationProto.Builder builder = LevelInformationProto
        .newBuilder()
        .clear()
        .setTotal(total)
        .setGiven(given)
        .setTaken(taken);

    if (Objects.nonNull(relationshipLevel.getLevel())) {
      builder.setLevel(relationshipLevel.getLevel());
    }
    if (Objects.nonNull(relationshipLevel.getTitle())) {
      builder.setTitle(relationshipLevel.getTitle());
    }
    if (Objects.nonNull(relationshipLevel.getDescription())) {
      builder.setDescription(relationshipLevel.getDescription());
    }

    return builder
        .build();
  }
}
