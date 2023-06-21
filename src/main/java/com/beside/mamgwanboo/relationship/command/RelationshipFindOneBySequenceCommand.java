package com.beside.mamgwanboo.relationship.command;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.relationship.document.Relationship;
import com.beside.mamgwanboo.relationship.repository.RelationshipRepository;
import reactor.core.publisher.Mono;

public class RelationshipFindOneBySequenceCommand {
  private final String userSequence;
  private final String sequence;
  private Mono<Relationship> result;

  public RelationshipFindOneBySequenceCommand(String userSequence, String sequence) {
    this.userSequence = userSequence;
    this.sequence = sequence;
  }

  public Mono<Relationship> execute(RelationshipRepository relationshipRepository) {
    result = relationshipRepository.findOneByUserSequenceAndSequenceAndUseYn(
        userSequence,
        sequence,
        YnType.Y
    );

    return result;
  }
}
