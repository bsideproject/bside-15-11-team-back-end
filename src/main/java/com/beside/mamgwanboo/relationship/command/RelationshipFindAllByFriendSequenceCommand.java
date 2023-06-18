package com.beside.mamgwanboo.relationship.command;

import com.beside.mamgwanboo.relationship.document.Relationship;
import com.beside.mamgwanboo.relationship.repository.CustomRelationshipRepository;
import java.util.UUID;
import protobuf.common.type.SortOrderType;
import reactor.core.publisher.Flux;

public class RelationshipFindAllByFriendSequenceCommand {
  private final UUID friendSequence;
  private final SortOrderType sortOrderType;
  private Flux<Relationship> result;

  public RelationshipFindAllByFriendSequenceCommand(
      UUID friendSequence,
      SortOrderType sortOrderType
  ) {
    this.friendSequence = friendSequence;
    this.sortOrderType = sortOrderType;
  }

  public Flux<Relationship> execute(CustomRelationshipRepository customRelationshipRepository) {
    result = customRelationshipRepository.findAllByFriendSequence(friendSequence, sortOrderType);

    return result;
  }
}
