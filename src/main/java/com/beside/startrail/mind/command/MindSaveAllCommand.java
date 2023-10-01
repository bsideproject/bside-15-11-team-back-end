package com.beside.startrail.mind.command;

import com.beside.startrail.common.command.Command;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.repository.MindRepository;
import java.util.List;
import reactor.core.publisher.Flux;

public class MindSaveAllCommand implements Command<Flux<Mind>, MindRepository> {
  private final List<Mind> minds;
  private Flux<Mind> result;

  public MindSaveAllCommand(List<Mind> minds) {
    this.minds = minds;
  }

  @Override
  public Flux<Mind> execute(MindRepository mindRepository) {
    result = mindRepository.saveAll(minds);

    return result;
  }
}
