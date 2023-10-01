package com.beside.startrail.relationshiplevel.service;

import com.beside.startrail.common.command.Command;
import com.beside.startrail.relationshiplevel.command.RelationshipLevelFindOneBetweenCountCommand;
import com.beside.startrail.relationshiplevel.document.RelationshipLevel;
import com.beside.startrail.relationshiplevel.repository.RelationshipLevelRepository;
import reactor.core.publisher.Mono;

public class RelationshipLevelService {
  public static Command<Mono<RelationshipLevel>, RelationshipLevelRepository> getBetweenCount(int count) {
    return new RelationshipLevelFindOneBetweenCountCommand(count);
  }
}
