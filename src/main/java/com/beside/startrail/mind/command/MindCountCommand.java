package com.beside.startrail.mind.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.model.MindCountResult;
import com.beside.startrail.mind.repository.CustomMindRepository;
import reactor.core.publisher.Mono;

public class MindCountCommand {
  private final String userSequence;
  private Mono<MindCountResult> result;

  public MindCountCommand(String userSequence) {
    this.userSequence = userSequence;
  }

  public Mono<MindCountResult> execute(
      CustomMindRepository customMindRepository
  ) {
    result = customMindRepository.countByUserSequenceAndUseYn(
        userSequence,
        YnType.Y);

    return result;
  }
}
