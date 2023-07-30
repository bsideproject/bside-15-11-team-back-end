package com.beside.startrail.mind.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.repository.MindRepository;
import reactor.core.publisher.Flux;

public class MindFindAllByUserSequenceCommand {
  private final String userSequence;
  private Flux<Mind> result;

  public MindFindAllByUserSequenceCommand(String userSequence) {
    this.userSequence = userSequence;
  }

  public Flux<Mind> execute(MindRepository mindRepository) {
    result = mindRepository.findByUserSequenceAndUseYn(
        userSequence,
        YnType.Y
    );

    return result;
  }
}
