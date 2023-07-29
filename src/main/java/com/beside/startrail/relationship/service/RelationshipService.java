package com.beside.startrail.relationship.service;

import com.beside.startrail.relationship.command.RelationshipCountCommand;
import com.beside.startrail.relationship.command.RelationshipDeleteOneCommand;
import com.beside.startrail.relationship.command.RelationshipFindAllByFriendSequenceCommand;
import com.beside.startrail.relationship.command.RelationshipFindBySequenceCommand;
import com.beside.startrail.relationship.command.RelationshipSaveAllCommand;
import com.beside.startrail.relationship.command.RelationshipSaveOneCommand;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.type.SortOrderType;
import java.util.List;

public class RelationshipService {
  public static RelationshipCountCommand countByUserSequenceAndRelationshipType(
      String userSequence) {
    return new RelationshipCountCommand(userSequence);
  }

  public static RelationshipFindAllByFriendSequenceCommand getByFriendSequence(
      String friendSequence,
      SortOrderType sortOrderType
  ) {
    return new RelationshipFindAllByFriendSequenceCommand(
        friendSequence,
        sortOrderType.getSort("date")
    );
  }

  public static RelationshipFindBySequenceCommand getByUserSequenceAndSequence(String sequence) {
    return new RelationshipFindBySequenceCommand(sequence);
  }

  public static RelationshipSaveAllCommand save(List<Relationship> relationships) {
    return new RelationshipSaveAllCommand(relationships);
  }

  public static RelationshipSaveOneCommand update(Relationship relationship) {
    return new RelationshipSaveOneCommand(relationship);
  }

  public static RelationshipDeleteOneCommand removeByUserSequenceAndSequence(
      String userSequence,
      String sequence
  ) {
    return new RelationshipDeleteOneCommand(userSequence, sequence);
  }
}
