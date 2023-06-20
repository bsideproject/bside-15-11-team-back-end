package com.beside.mamgwanboo.relationship.command;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.relationship.repository.RelationshipRepository;
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
