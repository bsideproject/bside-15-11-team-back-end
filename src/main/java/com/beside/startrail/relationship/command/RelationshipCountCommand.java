package com.beside.startrail.relationship.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.model.RelationshipCountResult;
import com.beside.startrail.relationship.repository.CustomRelationshipRepository;
import reactor.core.publisher.Mono;

public class RelationshipCountCommand {
  private final String userSequence;
  private Mono<RelationshipCountResult> result;

  public RelationshipCountCommand(String userSequence) {
    this.userSequence = userSequence;
  }

  public Mono<RelationshipCountResult> execute(
      CustomRelationshipRepository customRelationshipRepository
  ) {
    result = customRelationshipRepository.countByUserSequenceAndUseYn(
        userSequence,
        YnType.Y);

    return result;
  }
}
