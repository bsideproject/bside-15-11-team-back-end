package com.beside.mamgwanboo.relationship.util;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.common.util.DateUtil;
import com.beside.mamgwanboo.relationship.document.Relationship;
import protobuf.common.RelationshipRequestDto;
import protobuf.common.RelationshipResponseDto;

public class RelationshipDtoUtil {
  private RelationshipDtoUtil() {
  }

  public static RelationshipResponseDto toRelationshipResponseDto(Relationship relationship) {
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
