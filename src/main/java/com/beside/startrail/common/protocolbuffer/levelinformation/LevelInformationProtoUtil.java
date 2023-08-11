package com.beside.startrail.common.protocolbuffer.levelinformation;

import com.beside.startrail.relationshiplevel.document.RelationshipLevel;
import java.util.Objects;
import protobuf.common.LevelInformationProto;
import protobuf.common.RelationshipLevelProto;

public class LevelInformationProtoUtil {
  private LevelInformationProtoUtil() {
  }

  public static LevelInformationProto toLevelInformationProto(RelationshipLevel relationshipLevel) {
    if (Objects.isNull(relationshipLevel)) {
      return LevelInformationProto
          .newBuilder()
          .clear()
          .build();
    }

    LevelInformationProto.Builder builder = LevelInformationProto
        .newBuilder()
        .clear();

    if (Objects.nonNull(relationshipLevel.getSequence())) {
      builder.setSequence(relationshipLevel.getSequence());
    }
    if (Objects.nonNull(relationshipLevel.getTitle())) {
      builder.setTitle(relationshipLevel.getTitle());
    }
    if (Objects.nonNull(relationshipLevel.getDescription())) {
      builder.setDescription(relationshipLevel.getDescription());
    }
    if (Objects.nonNull(relationshipLevel.getLevel())) {
      builder.setLevel(relationshipLevel.getLevel());
    }
    if (Objects.nonNull(relationshipLevel.getCountFrom())) {
      builder.setCountFrom(relationshipLevel.getCountFrom());
    }
    if (Objects.nonNull(relationshipLevel.getCountTo())) {
      builder.setCountTo(relationshipLevel.getCountTo());
    }

    return builder
        .build();
  }

  public static RelationshipLevel toRelationshipLevel(
      RelationshipLevelProto relationshipLevelProto
  ) {
    if (Objects.isNull(relationshipLevelProto) ||
        !relationshipLevelProto.isInitialized()) {
      return null;
    }

    return RelationshipLevel.builder()
        .sequence(relationshipLevelProto.getSequence())
        .title(relationshipLevelProto.getTitle())
        .description(relationshipLevelProto.getDescription())
        .level(relationshipLevelProto.getLevel())
        .countFrom(relationshipLevelProto.getCountFrom())
        .countTo(relationshipLevelProto.getCountTo())
        .build();
  }
}
