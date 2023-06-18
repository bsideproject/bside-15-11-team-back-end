package com.beside.mamgwanboo.relationship.command;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.relationship.repository.RelationshipRepository;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class RelationshipCountCommand {
  private final UUID userSequence;
  private Mono<Integer> result;

  public RelationshipCountCommand(UUID userSequence) {
    this.userSequence = userSequence;
  }

  public Mono<Integer> execute(RelationshipRepository relationshipRepository) {
    result = relationshipRepository.countAllByUserSequenceAndUseYn(userSequence, YnType.Y);

    return result;
  }
}
