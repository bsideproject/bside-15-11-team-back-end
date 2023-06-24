package com.beside.startrail.common.protocolbuffer.relationship;

import com.beside.startrail.common.protocolbuffer.common.DateProtoUtil;
import com.beside.startrail.common.protocolbuffer.common.ItemProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.type.RelationshipType;
import java.util.Objects;
import protobuf.common.RelationshipRequestProto;
import protobuf.common.RelationshipResponseProto;
import protobuf.common.type.RelationshipTypeProto;

public class RelationshipProtoUtil {
  private RelationshipProtoUtil() {
  }

  public static RelationshipResponseProto toRelationshipResponseProto(Relationship relationship) {
    if (Objects.isNull(relationship)) {
      return RelationshipResponseProto.newBuilder().build();
    }

    return RelationshipResponseProto.newBuilder()
        .setSequence(relationship.getSequence())
        .setType(
            RelationshipTypeProto.valueOf(relationship.getType().name())
        )
        .setEvent(relationship.getEvent())
        .setDate(DateProtoUtil.toDate(relationship.getDate()))
        .setCreateDate(DateProtoUtil.toDate(relationship.getCreateDate()))
        .setModifyDate(DateProtoUtil.toDate(relationship.getModifyDate()))
        .setItem(ItemProtoUtil.toItemProto(relationship.getItem()))
        .setMemo(relationship.getMemo())
        .build();
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
}
