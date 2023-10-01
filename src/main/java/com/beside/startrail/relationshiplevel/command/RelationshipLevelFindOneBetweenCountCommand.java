package com.beside.startrail.relationshiplevel.command;

import com.beside.startrail.common.command.Command;
import com.beside.startrail.relationshiplevel.document.RelationshipLevel;
import com.beside.startrail.relationshiplevel.repository.RelationshipLevelRepository;
import reactor.core.publisher.Mono;

public class RelationshipLevelFindOneBetweenCountCommand
    implements Command<Mono<RelationshipLevel>, RelationshipLevelRepository> {
  private final int count;

  private Mono<RelationshipLevel> result;

  public RelationshipLevelFindOneBetweenCountCommand(int count) {
    this.count = count;
  }

  @Override
  public Mono<RelationshipLevel> execute(RelationshipLevelRepository relationshipLevelRepository) {
    result = relationshipLevelRepository.findByCountFromIsLessThanEqualAndCountToIsGreaterThanEqual(
        count,
        count
    );

    return result;
  }
}
