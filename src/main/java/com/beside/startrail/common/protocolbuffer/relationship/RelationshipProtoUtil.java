package com.beside.startrail.common.protocolbuffer.relationship;

import com.beside.startrail.common.protocolbuffer.common.DateProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.document.Birth;
import com.beside.startrail.relationship.document.Relationship;
import java.util.Objects;
import protobuf.common.BirthProto;
import protobuf.common.LevelInformationProto;
import protobuf.common.type.YnTypeProto;
import protobuf.relationship.RelationshipPostRequestProto;
import protobuf.relationship.RelationshipPutRequestProto;
import protobuf.relationship.RelationshipResponseProto;
import reactor.core.publisher.Flux;

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
    if (Objects.nonNull(relationship.getFavoriteYn())) {
      builder.setFavoriteYn(YnTypeProto.valueOf(relationship.getFavoriteYn().name()));
    }

    return builder.build();
  }

  public static RelationshipResponseProto from(
      RelationshipResponseProto relationshipResponseProto,
      LevelInformationProto levelInformationProto
  ) {
    return relationshipResponseProto
        .toBuilder()
        .setLevelInformation(levelInformationProto)
        .build();
  }

  public static Flux<Relationship> toRelationships(
      String userSequence,
      RelationshipPostRequestProto relationshipPostRequestProto
  ) {
    if (Objects.isNull(relationshipPostRequestProto) ||
        !relationshipPostRequestProto.isInitialized()) {
      return Flux.empty();
    }

    return Flux.fromStream(
        relationshipPostRequestProto.getNicknamesList().stream()
            .map(nickname -> Relationship.builder()
                .userSequence(userSequence)
                .nickname(nickname)
                .relationship(relationshipPostRequestProto.getRelationship())
                .birth(toBirth(relationshipPostRequestProto.getBirth()))
                .memo(relationshipPostRequestProto.getMemo())
                .build()
            )
    );
  }

  public static Relationship toRelationship(
      Relationship relationship,
      RelationshipPutRequestProto relationshipPutRequestProto
  ) {
    if (Objects.isNull(relationshipPutRequestProto) ||
        !relationshipPutRequestProto.isInitialized()) {
      return null;
    }

    return Relationship.builder()
        .sequence(relationship.getSequence())
        .userSequence(relationship.getUserSequence())
        .nickname(relationshipPutRequestProto.getNickname())
        .relationship(relationshipPutRequestProto.getRelationship())
        .birth(toBirth(relationshipPutRequestProto.getBirth()))
        .memo(relationshipPutRequestProto.getMemo())
        .favoriteYn(YnType.valueOf(relationshipPutRequestProto.getFavoriteYn().name()))
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
