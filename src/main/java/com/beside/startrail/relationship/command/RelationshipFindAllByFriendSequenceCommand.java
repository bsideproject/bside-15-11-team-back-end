package com.beside.startrail.relationship.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;

public class RelationshipFindAllByFriendSequenceCommand {
  private final String friendSequence;
  private final Sort sort;
  private Flux<Relationship> result;

  public RelationshipFindAllByFriendSequenceCommand(
      String friendSequence,
      Sort sort
  ) {
    this.friendSequence = friendSequence;
    this.sort = sort;
  }

  public Flux<Relationship> execute(RelationshipRepository relationshipRepository) {
    result = relationshipRepository.findAllByFriendSequenceAndUseYn(
        friendSequence,
        YnType.Y,
        sort
    );

    return result;
  }
}
