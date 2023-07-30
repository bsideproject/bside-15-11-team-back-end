package com.beside.startrail.common.protocolbuffer.relationship;

import com.beside.startrail.common.protocolbuffer.common.DateProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.document.Birth;
import com.beside.startrail.relationship.document.Relationship;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import protobuf.common.BirthProto;
import protobuf.common.type.YnTypeProto;
import protobuf.relationship.RelationshipPostRequestProto;
import protobuf.relationship.RelationshipPutRequestProto;
import protobuf.relationship.RelationshipResponseProto;

public class RelationshipProtoUtil {

  public static RelationshipResponseProto toRelationshipResponseProto(Relationship relationship) {
    if (Objects.isNull(relationship)) {
      return RelationshipResponseProto
          .newBuilder()
          .clear()
          .build();
    }

    RelationshipResponseProto.Builder builder = RelationshipResponseProto
        .newBuilder()
        .clear();

    if (Objects.nonNull(relationship.getSequence())) {
      builder.setSequence(relationship.getSequence());
    }
    if (Objects.nonNull(relationship.getNickname())) {
      builder.setNickname(relationship.getNickname());
    }
    if (Objects.nonNull(relationship.getRelationship())) {
      builder.setRelationship(relationship.getRelationship());
    }
    if (Objects.nonNull(relationship.getBirth())) {
      builder.setBirth(toBirthProto(relationship.getBirth()));
    }
    if (Objects.nonNull(relationship.getMemo())) {
      builder.setMemo(relationship.getMemo());
    }

    return builder.build();
  }

  public static List<Relationship> toRelationships(
      String userSequence,
      RelationshipPostRequestProto relationshipPostRequestProto
  ) {
    if (Objects.isNull(relationshipPostRequestProto) ||
        !relationshipPostRequestProto.isInitialized()) {
      return null;
    }

    return relationshipPostRequestProto.getNicknamesList().stream()
        .map(nickname -> Relationship.builder()
            .userSequence(userSequence)
            .nickname(nickname)
            .relationship(relationshipPostRequestProto.getRelationship())
            .birth(toBirth(relationshipPostRequestProto.getBirth()))
            .memo(relationshipPostRequestProto.getMemo())
            .build()
        )
        .collect(Collectors.toList());
  }

  public static Relationship toRelationships(
      Relationship friend,
      RelationshipPutRequestProto relationshipPutRequestProto
  ) {
    if (Objects.isNull(relationshipPutRequestProto) ||
        !relationshipPutRequestProto.isInitialized()) {
      return null;
    }

    return Relationship.builder()
        .sequence(friend.getSequence())
        .userSequence(friend.getUserSequence())
        .nickname(relationshipPutRequestProto.getNickname())
        .relationship(relationshipPutRequestProto.getRelationship())
        .birth(toBirth(relationshipPutRequestProto.getBirth()))
        .memo(relationshipPutRequestProto.getMemo())
        .build();
  }

  public static Birth toBirth(BirthProto birthProto) {
    if (Objects.isNull(birthProto) || !birthProto.isInitialized()) {
      return null;
    }

    return Birth.builder()
        .isLunar(YnType.valueOf(birthProto.getIsLunar().name()))
        .date(DateProtoUtil.toLocalDate(birthProto.getDate()))
        .build();
  }

  private static BirthProto toBirthProto(Birth birth) {
    if (Objects.isNull(birth)) {
      return BirthProto.newBuilder()
          .clear()
          .build();
    }

    BirthProto.Builder builder = BirthProto
        .newBuilder()
        .clear();

    if (Objects.nonNull(birth.getIsLunar())) {
      builder.setIsLunar(YnTypeProto.valueOf(birth.getIsLunar().name()));
    }
    if (Objects.nonNull(birth.getDate())) {
      builder.setDate(DateProtoUtil.toDate(birth.getDate()));
    }

    return builder.build();
  }
}
