package com.beside.startrail.mind.command;

import com.beside.startrail.common.command.Command;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.model.MindCountResult;
import com.beside.startrail.mind.repository.CustomMindRepository;
import reactor.core.publisher.Mono;

public class MindCountByUserSequenceAndUseYnCommand
    implements Command<Mono<MindCountResult>, CustomMindRepository> {
  private final String userSequence;
  private final YnType useYn;

  private Mono<MindCountResult> result;

  public MindCountByUserSequenceAndUseYnCommand(String userSequence, YnType useYn) {
    this.userSequence = userSequence;
    this.useYn = useYn;
  }
  @Override
  public Mono<MindCountResult> execute(
      CustomMindRepository customMindRepository
  ) {
    result = customMindRepository.countByUserSequenceAndUseYn(
        userSequence,
        useYn
    );

    return result;
  }
}
