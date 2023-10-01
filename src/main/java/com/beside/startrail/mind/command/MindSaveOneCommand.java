package com.beside.startrail.mind.command;

import com.beside.startrail.common.command.Command;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.repository.MindRepository;
import reactor.core.publisher.Mono;

public class MindSaveOneCommand implements Command<Mono<Mind>, MindRepository> {
  private final Mind mind;
  private Mono<Mind> result;

  public MindSaveOneCommand(Mind mind) {
    this.mind = mind;
  }

  @Override
  public Mono<Mind> execute(MindRepository mindRepository) {
    result = mindRepository.save(mind);

    return result;
  }
}
