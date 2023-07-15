package com.beside.startrail.relationship.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import reactor.core.publisher.Flux;

public class RelationshipFindByUserSequenceCommand {
  private final String userSequence;
  private Flux<Relationship> result;

  public RelationshipFindByUserSequenceCommand(String userSequence) {
    this.userSequence = userSequence;
  }

  public Flux<Relationship> execute(RelationshipRepository relationshipRepository) {
    result = relationshipRepository.findByUserSequenceAndUseYn(
        userSequence,
        YnType.Y
    );

    return result;
  }
}
