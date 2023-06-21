package com.beside.startrail.relationship.util;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.common.util.DateUtil;
import com.beside.startrail.relationship.document.Relationship;
import java.util.Objects;
import protobuf.common.RelationshipRequestDto;
import protobuf.common.RelationshipResponseDto;

public class RelationshipDtoUtil {
  private RelationshipDtoUtil() {
  }

  public static RelationshipResponseDto toRelationshipResponseDto(Relationship relationship) {
    if (Objects.isNull(relationship)) {
      return RelationshipResponseDto.newBuilder().build();
    }

    return RelationshipResponseDto.newBuilder()
        .setSequence(relationship.getSequence())
        .setType(relationship.getType())
        .setEvent(relationship.getEvent())
        .setDate(DateUtil.toDate(relationship.getDate()))
        .setCreateDate(DateUtil.toDate(relationship.getCreateDate()))
        .setModifyDate(DateUtil.toDate(relationship.getModifyDate()))
        .setItem(ItemUtil.toItemDto(relationship.getItem()))
        .setMemo(relationship.getMemo())
        .build();
  }

  public static Relationship toRelationship(
      RelationshipRequestDto relationshipDto,
      String userSequence,
      YnType useYn
  ) {
    if (Objects.isNull(relationshipDto)) {
      return null;
    }

    return Relationship.builder()
        .userSequence(userSequence)
        .friendSequence(relationshipDto.getFriendSequence())
        .type(relationshipDto.getType())
        .event(relationshipDto.getEvent())
        .date(DateUtil.toLocalDateTime(relationshipDto.getDate()))
        .item(ItemUtil.toItem(relationshipDto.getItem()))
        .memo(relationshipDto.getMemo())
        .useYn(useYn)
        .build();
  }
}
