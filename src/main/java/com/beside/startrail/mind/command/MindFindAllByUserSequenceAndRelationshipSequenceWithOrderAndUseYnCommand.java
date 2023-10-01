package com.beside.startrail.mind.command;

import com.beside.startrail.common.command.Command;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.repository.MindRepository;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;

public class MindFindAllByUserSequenceAndRelationshipSequenceWithOrderAndUseYnCommand
    implements Command<Flux<Mind>, MindRepository> {
  private final String userSequence;
  private final String relationshipSequence;
  private final Sort sort;
  private final YnType useYn;

  private Flux<Mind> result;

  public MindFindAllByUserSequenceAndRelationshipSequenceWithOrderAndUseYnCommand(
      String userSequence,
      String relationshipSequence,
      Sort sort,
      YnType useYn
  ) {
    this.userSequence = userSequence;
    this.relationshipSequence = relationshipSequence;
    this.sort = sort;
    this.useYn = useYn;
  }
  @Override
  public Flux<Mind> execute(MindRepository mindRepository) {
    result = mindRepository.findAllByUserSequenceAndRelationshipSequenceAndUseYn(
        userSequence,
        relationshipSequence,
        useYn,
        sort
    );

    return result;
  }
}
