package com.beside.startrail.mind.command;

import com.beside.startrail.common.command.Command;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.repository.MindRepository;
import reactor.core.publisher.Mono;

public class MindFindOneByUserSequenceAndSequenceAndUseYnCommand
    implements Command<Mono<Mind>, MindRepository> {
  private final String userSequence;
  private final String sequence;
  private final YnType useYn;

  private Mono<Mind> result;

  public MindFindOneByUserSequenceAndSequenceAndUseYnCommand(
      String userSequence,
      String sequence,
      YnType useYn
  ) {
    this.userSequence = userSequence;
    this.sequence = sequence;
    this.useYn = useYn;
  }

  @Override
  public Mono<Mind> execute(MindRepository mindRepository) {
    result = mindRepository.findOneByUserSequenceAndSequenceAndUseYn(
        userSequence,
        sequence,
        useYn
    );

    return result;
  }
}
