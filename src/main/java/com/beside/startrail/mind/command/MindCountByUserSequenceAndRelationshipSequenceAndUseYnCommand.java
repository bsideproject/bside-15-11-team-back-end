package com.beside.startrail.mind.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.model.MindCountResult;
import com.beside.startrail.mind.repository.CustomMindRepository;
import reactor.core.publisher.Mono;

public class MindCountByUserSequenceAndRelationshipSequenceAndUseYnCommand {
  private final String userSequence;
  private final String relationshipSequence;
  private final YnType useYn;

  private Mono<MindCountResult> result;

  public MindCountByUserSequenceAndRelationshipSequenceAndUseYnCommand(
      String userSequence,
      String relationshipSequence,
      YnType useYn
  ) {
    this.userSequence = userSequence;
    this.relationshipSequence = relationshipSequence;
    this.useYn = useYn;
  }

  public Mono<MindCountResult> execute(CustomMindRepository customMindRepository) {
    result = customMindRepository.countByUserSequenceAndRelationshipSequenceAndUseYn(
        userSequence,
        relationshipSequence,
        useYn
    );

    return result;
  }
}
