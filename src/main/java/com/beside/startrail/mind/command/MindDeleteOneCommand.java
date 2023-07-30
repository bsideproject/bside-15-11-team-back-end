package com.beside.startrail.mind.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.repository.MindRepository;
import reactor.core.publisher.Mono;

public class MindDeleteOneCommand {
  private final String userSequence;
  private final String sequence;
  private Mono<Mind> result;

  public MindDeleteOneCommand(String userSequence, String sequence) {
    this.userSequence = userSequence;
    this.sequence = sequence;
  }

  public Mono<Mind> execute(MindRepository mindRepository) {
    result = mindRepository.deleteByUserSequenceAndSequenceAndUseYn(
        userSequence,
        sequence,
        YnType.Y
    );

    return result;
  }
}
