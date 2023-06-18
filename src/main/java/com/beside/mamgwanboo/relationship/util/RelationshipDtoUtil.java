package com.beside.mamgwanboo.relationship.util;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.common.util.DateUtil;
import com.beside.mamgwanboo.relationship.document.Relationship;
import protobuf.common.RelationshipDto;

public class RelationshipDtoUtil {
  private RelationshipDtoUtil() {
  }

  public static RelationshipDto toRelationshipDto(Relationship relationship) {
    return RelationshipDto.newBuilder()
        .setSequence(relationship.getSequence())
        .setFriendSequence(relationship.getFriendSequence())
        .setType(relationship.getType())
        .setEvent(relationship.getEvent())
        .setCreateDate(DateUtil.toDate(relationship.getCreateDate()))
        .setModifyDate(DateUtil.toDate(relationship.getModifyDate()))
        .setItem(relationship.getItem())
        .setMemo(relationship.getMemo())
        .build();
  }

  public static Relationship toRelationship(
      RelationshipDto relationshipDto,
      String userSequence,
      YnType useYn
  ) {
    Relationship.RelationshipBuilder relationshipBuilder = Relationship.builder()
        .userSequence(userSequence)
        .friendSequence(relationshipDto.getFriendSequence())
        .type(relationshipDto.getType())
        .event(relationshipDto.getEvent())
        .createDate(DateUtil.toLocalDateTime(relationshipDto.getCreateDate()))
        .modifyDate(DateUtil.toLocalDateTime(relationshipDto.getModifyDate()))
        .item(relationshipDto.getItem())
        .memo(relationshipDto.getMemo())
        .useYn(useYn);

    if (relationshipDto.hasField(RelationshipDto.getDescriptor().findFieldByName("sequence"))) {
      relationshipBuilder.sequence(relationshipDto.getSequence());
    }

    return relationshipBuilder
        .build();
  }
}
