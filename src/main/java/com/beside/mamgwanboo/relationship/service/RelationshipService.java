package com.beside.mamgwanboo.relationship.service;

import com.beside.mamgwanboo.common.util.SortOrderTypeUtil;
import com.beside.mamgwanboo.relationship.command.RelationshipCountCommand;
import com.beside.mamgwanboo.relationship.command.RelationshipDeleteCommand;
import com.beside.mamgwanboo.relationship.command.RelationshipFindAllByFriendSequenceCommand;
import com.beside.mamgwanboo.relationship.command.RelationshipFindOneBySequenceCommand;
import com.beside.mamgwanboo.relationship.command.RelationshipSaveAllCommand;
import com.beside.mamgwanboo.relationship.command.RelationshipSaveCommand;
import com.beside.mamgwanboo.relationship.document.Relationship;
import java.util.List;
import protobuf.common.type.SortOrderType;

public class RelationshipService {
  public static RelationshipCountCommand countByUserSequence(String userSequence) {
    return new RelationshipCountCommand(userSequence);
  }

  public static RelationshipFindAllByFriendSequenceCommand getByFriendSequence(
      String friendSequence,
      SortOrderType sortOrderType
  ) {
    return new RelationshipFindAllByFriendSequenceCommand(
        friendSequence,
        SortOrderTypeUtil.toSort("date", sortOrderType)
    );
  }

  public static RelationshipFindOneBySequenceCommand getBySequence(String sequence) {
    return new RelationshipFindOneBySequenceCommand(sequence);
  }

  public static RelationshipSaveAllCommand save(List<Relationship> relationships) {
    return new RelationshipSaveAllCommand(relationships);
  }

  public static RelationshipSaveCommand update(Relationship relationship) {
    return new RelationshipSaveCommand(relationship);
  }

  public static RelationshipDeleteCommand remove(String sequence) {
    return new RelationshipDeleteCommand(sequence);
  }
}
