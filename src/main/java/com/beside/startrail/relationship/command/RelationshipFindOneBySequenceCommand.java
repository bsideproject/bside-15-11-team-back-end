package com.beside.startrail.relationship.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import reactor.core.publisher.Mono;

public class RelationshipFindOneBySequenceCommand {
  private final String sequence;
  private Mono<Relationship> result;

  public RelationshipFindOneBySequenceCommand(String sequence) {
    this.sequence = sequence;
  }

  public Mono<Relationship> execute(RelationshipRepository relationshipRepository) {
    result = relationshipRepository.findOneBySequenceAndUseYn(
        sequence,
        YnType.Y
    );

    return result;
  }
}
