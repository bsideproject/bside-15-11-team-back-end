package com.beside.startrail.common.protocolbuffer.relationship;

import com.beside.startrail.common.protocolbuffer.common.DateProtoUtil;
import com.beside.startrail.common.protocolbuffer.common.ItemProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.model.RelationshipCountResult;
import com.beside.startrail.relationship.type.RelationshipType;
import java.util.Objects;
import protobuf.common.RelationshipRequestProto;
import protobuf.common.RelationshipResponseProto;
import protobuf.common.type.RelationshipTypeProto;
import protobuf.relationship.RelationshipCountResponseProto;
import protobuf.relationship.RelationshipPutRequestProto;
import protobuf.relationship.RelationshipPutResponseProto;

public class RelationshipProtoUtil {
  private RelationshipProtoUtil() {
  }

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
    if (Objects.nonNull(relationship.getFriendSequence())) {
      builder.setFriendSequence(relationship.getFriendSequence());
    }
    if (Objects.nonNull(relationship.getType())) {
      builder.setType(
          RelationshipTypeProto.valueOf(relationship.getType().name())
      );
    }
    if (Objects.nonNull(relationship.getEvent())) {
      builder.setEvent(relationship.getEvent());
    }
    if (Objects.nonNull(relationship.getDate())) {
      builder.setDate(DateProtoUtil.toDate(relationship.getDate()));
    }
    if (Objects.nonNull(relationship.getItem())) {
      builder.setItem(ItemProtoUtil.toItemProto(relationship.getItem()));
    }
    if (Objects.nonNull(relationship.getMemo())) {
      builder.setMemo(relationship.getMemo());
    }

    return builder.build();
  }

  public static Relationship toRelationship(
      RelationshipRequestProto relationshipRequestProto,
      String userSequence,
      YnType useYn
  ) {
    if (Objects.isNull(relationshipRequestProto)) {
      return null;
    }

    return Relationship.builder()
        .userSequence(userSequence)
        .friendSequence(relationshipRequestProto.getFriendSequence())
        .type(
            RelationshipType.valueOf(relationshipRequestProto.getType().name())
        )
        .event(relationshipRequestProto.getEvent())
        .date(DateProtoUtil.toLocalDateTime(relationshipRequestProto.getDate()))
        .item(ItemProtoUtil.toItem(relationshipRequestProto.getItem()))
        .memo(relationshipRequestProto.getMemo())
        .useYn(useYn)
        .build();
  }

  public static RelationshipCountResponseProto toRelationshipCountResponseProto(
      RelationshipCountResult relationshipCountResult
  ) {
    if (Objects.isNull(relationshipCountResult)) {
      return RelationshipCountResponseProto.newBuilder()
          .clear()
          .build();
    }

    return RelationshipCountResponseProto.newBuilder()
        .setTotal(relationshipCountResult.getTotal())
        .setGiven(relationshipCountResult.getGiven())
        .setTaken(relationshipCountResult.getTaken())
        .build();
  }

  public static Relationship toRelationship(
      RelationshipPutRequestProto relationshipPutRequestProto,
      String sequence,
      YnType ynType
  ) {
    if (Objects.isNull(relationshipPutRequestProto) ||
        !relationshipPutRequestProto.isInitialized()) {
      return null;
    }

    return Relationship.builder()
        .sequence(relationshipPutRequestProto.getSequence())
        .userSequence(sequence)
        .friendSequence(relationshipPutRequestProto.getFriendSequence())
        .type(
            RelationshipType.valueOf(relationshipPutRequestProto.getType().name())
        )
        .event(relationshipPutRequestProto.getEvent())
        .date(DateProtoUtil.toLocalDateTime(relationshipPutRequestProto.getDate()))
        .item(ItemProtoUtil.toItem(relationshipPutRequestProto.getItem()))
        .memo(relationshipPutRequestProto.getMemo())
        .useYn(ynType)
        .build();
  }

  public static RelationshipPutResponseProto toRelationshipPutResponseProto(
      Relationship relationship) {
    if (Objects.isNull(relationship)) {
      return RelationshipPutResponseProto.newBuilder()
          .clear()
          .build();
    }

    RelationshipPutResponseProto.Builder builder = RelationshipPutResponseProto
        .newBuilder()
        .clear();

    if (Objects.nonNull(relationship.getSequence())) {
      builder.setSequence(relationship.getSequence());
    }
    if (Objects.nonNull(relationship.getType())) {
      builder.setType(
          RelationshipTypeProto.valueOf(relationship.getType().name())
      );
    }
    if (Objects.nonNull(relationship.getEvent())) {
      builder.setEvent(relationship.getEvent());
    }
    if (Objects.nonNull(relationship.getDate())) {
      builder.setDate(DateProtoUtil.toDate(relationship.getDate()));
    }
    if (Objects.nonNull(relationship.getItem())) {
      builder.setItem(ItemProtoUtil.toItemProto(relationship.getItem()));
    }
    if (Objects.nonNull(relationship.getMemo())) {
      builder.setMemo(relationship.getMemo());
    }

    return builder.build();
  }
}
