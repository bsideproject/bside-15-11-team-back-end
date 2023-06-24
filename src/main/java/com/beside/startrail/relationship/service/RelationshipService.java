package com.beside.startrail.relationship.service;

import com.beside.startrail.relationship.command.RelationshipCountCommand;
import com.beside.startrail.relationship.command.RelationshipDeleteCommand;
import com.beside.startrail.relationship.command.RelationshipFindAllByFriendSequenceCommand;
import com.beside.startrail.relationship.command.RelationshipFindOneBySequenceCommand;
import com.beside.startrail.relationship.command.RelationshipSaveAllCommand;
import com.beside.startrail.relationship.command.RelationshipSaveCommand;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.type.SortOrderType;
import java.util.List;

public class RelationshipService {
  public static RelationshipCountCommand countByUserSequence(String userSequence) {
    return new RelationshipCountCommand(userSequence);
  }

  public static RelationshipFindAllByFriendSequenceCommand getByFriendSequence(
      String userSequence,
      String friendSequence,
      SortOrderType sortOrderType
  ) {
    return new RelationshipFindAllByFriendSequenceCommand(
        userSequence,
        friendSequence,
        sortOrderType.getSort("date")
    );
  }

  public static RelationshipFindOneBySequenceCommand getByUserSequenceAndSequence(
      String userSequence, String sequence) {
    return new RelationshipFindOneBySequenceCommand(userSequence, sequence);
  }

  public static RelationshipSaveAllCommand save(List<Relationship> relationships) {
    return new RelationshipSaveAllCommand(relationships);
  }

  public static RelationshipSaveCommand update(Relationship relationship) {
    return new RelationshipSaveCommand(relationship);
  }

  public static RelationshipDeleteCommand removeByUserSequenceAndSequence(
      String userSequence,
      String sequence
  ) {
    return new RelationshipDeleteCommand(userSequence, sequence);
  }
}
