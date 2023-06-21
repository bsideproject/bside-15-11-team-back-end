package com.beside.startrail.relationship.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import reactor.core.publisher.Mono;

public class RelationshipCountCommand {
  private final String userSequence;
  private Mono<Long> result;

  public RelationshipCountCommand(String userSequence) {
    this.userSequence = userSequence;
  }

  public Mono<Long> execute(RelationshipRepository relationshipRepository) {
    result = relationshipRepository.countAllByUserSequenceAndUseYn(userSequence, YnType.Y);

    return result;
  }
}
