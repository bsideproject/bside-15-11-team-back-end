package com.beside.startrail.mind.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.repository.MindRepository;
import reactor.core.publisher.Mono;

public class MindFindOneBySequenceCommand {
  private final String sequence;
  private Mono<Mind> result;

  public MindFindOneBySequenceCommand(String sequence) {
    this.sequence = sequence;
  }

  public Mono<Mind> execute(MindRepository mindRepository) {
    result = mindRepository.findOneBySequenceAndUseYn(
        sequence,
        YnType.Y
    );

    return result;
  }
}
