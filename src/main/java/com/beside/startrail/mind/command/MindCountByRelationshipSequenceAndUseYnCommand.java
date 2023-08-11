package com.beside.startrail.mind.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.model.MindCountResult;
import com.beside.startrail.mind.repository.CustomMindRepository;
import com.beside.startrail.mind.repository.MindRepository;
import reactor.core.publisher.Mono;

public class MindCountByRelationshipSequenceAndUseYnCommand {
  private final String relationshipSequence;
  private final YnType useYn;
  private Mono<Integer> result;

  public MindCountByRelationshipSequenceAndUseYnCommand(String relationshipSequence, YnType useYn) {
    this.relationshipSequence = relationshipSequence;
    this.useYn = useYn;
  }

  public Mono<Integer> execute(
      MindRepository mindRepository
  ) {
    result = mindRepository.countByRelationshipSequenceAndUseYn(
        relationshipSequence,
        useYn
    );

    return result;
  }
}
