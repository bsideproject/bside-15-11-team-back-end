package com.beside.startrail.mind.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.repository.MindRepository;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;

public class MindFindAllByRelationshipSequenceWithOrderCommand {
  private final String userSequence;
  private final String relationshipSequence;
  private final Sort sort;

  private Flux<Mind> result;

  public MindFindAllByRelationshipSequenceWithOrderCommand(
      String userSequence,
      String relationshipSequence,
      Sort sort
  ) {
    this.userSequence = userSequence;
    this.relationshipSequence = relationshipSequence;
    this.sort = sort;
  }

  public Flux<Mind> execute(MindRepository mindRepository) {
    result = mindRepository.findAllByUserSequenceAndRelationshipSequenceAndUseYn(
        userSequence,
        relationshipSequence,
        YnType.Y,
        sort
    );

    return result;
  }
}
