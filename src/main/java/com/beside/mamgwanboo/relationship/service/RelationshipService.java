package com.beside.mamgwanboo.relationship.service;

import com.beside.mamgwanboo.relationship.command.RelationshipCountCommand;
import com.beside.mamgwanboo.relationship.command.RelationshipFindAllByFriendSequenceCommand;
import com.beside.mamgwanboo.relationship.command.RelationshipFindOneBySequenceCommand;
import com.beside.mamgwanboo.relationship.command.RelationshipSaveCommand;
import com.beside.mamgwanboo.relationship.document.Relationship;
import com.beside.mamgwanboo.relationship.util.RelationshipDtoUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import protobuf.common.type.SortOrderType;
import protobuf.relationship.RelationshipPutResponse;

public class RelationshipService {
  public static RelationshipCountCommand countByUserSequence(UUID userSequence) {
    return new RelationshipCountCommand(userSequence);
  }

  public static RelationshipFindAllByFriendSequenceCommand getByFriendSequence(
      UUID friendSequence,
      SortOrderType sortOrderType
  ) {
    return new RelationshipFindAllByFriendSequenceCommand(friendSequence, sortOrderType);
  }

  public static RelationshipFindOneBySequenceCommand getBySequence(UUID sequence) {
    return new RelationshipFindOneBySequenceCommand(sequence);
  }

  public static RelationshipSaveCommand save(List<Relationship> relationships) {
    return new RelationshipSaveCommand(relationships);
  }

  public static RelationshipPutResponse makeRelationshipPutResponse(
      List<Relationship> requestedRelationships,
      List<Relationship> successRelationships
  ) {
    List<Relationship> failRelationships = new ArrayList<>();
    for (Relationship relationship : requestedRelationships) {
      if (!successRelationships.contains(relationship)) {
        failRelationships.add(relationship);
      }
    }

    return RelationshipPutResponse.newBuilder()
        .addAllSuccessSequences(
            successRelationships.stream()
                .map(RelationshipDtoUtil::toRelationshipDto)
                .toList()
        )
        .addAllFailSequences(
            failRelationships.stream()
                .map(RelationshipDtoUtil::toRelationshipDto)
                .toList()
        )
        .build();
  }
}
