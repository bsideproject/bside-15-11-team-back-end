package com.beside.startrail.mind.service;

import com.beside.startrail.common.command.Command;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.command.MindCountByUserSequenceAndRelationshipSequenceAndUseYnCommand;
import com.beside.startrail.mind.command.MindCountByUserSequenceAndUseYnCommand;
import com.beside.startrail.mind.command.MindFindAllByUserSequenceAndRelationshipSequenceAndUseYnCommand;
import com.beside.startrail.mind.command.MindFindAllByUserSequenceAndRelationshipSequenceWithOrderAndUseYnCommand;
import com.beside.startrail.mind.command.MindFindAllByUserSequenceAndUseYnCommand;
import com.beside.startrail.mind.command.MindFindOneByUserSequenceAndSequenceAndUseYnCommand;
import com.beside.startrail.mind.command.MindSaveAllCommand;
import com.beside.startrail.mind.command.MindSaveOneCommand;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.model.MindCountResult;
import com.beside.startrail.mind.repository.CustomMindRepository;
import com.beside.startrail.mind.repository.MindRepository;
import com.beside.startrail.mind.type.SortOrderType;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MindService {
  public static Command<Mono<MindCountResult>, CustomMindRepository> countByUserSequence(
      String userSequence,
      YnType useYn
  ) {
    return new MindCountByUserSequenceAndUseYnCommand(userSequence, useYn);
  }

  public static Command<Mono<MindCountResult>, CustomMindRepository> countByRelationshipSequence(
      String userSequence,
      String relationshipSequence,
      YnType useYn
  ) {
    return new MindCountByUserSequenceAndRelationshipSequenceAndUseYnCommand(
        userSequence,
        relationshipSequence,
        useYn
    );
  }

  public static Command<Flux<Mind>, MindRepository> getByRelationshipSequenceWithOrder(
      String userSequence,
      String relationshipSequence,
      SortOrderType sortOrderType,
      YnType useYn
  ) {
    return new MindFindAllByUserSequenceAndRelationshipSequenceWithOrderAndUseYnCommand(
        userSequence,
        relationshipSequence,
        sortOrderType.getSort("date"),
        useYn
    );
  }

  public static Command<Flux<Mind>, MindRepository> getByRelationshipSequence(
      String userSequence,
      String relationshipSequence,
      YnType useYn
  ) {
    return new MindFindAllByUserSequenceAndRelationshipSequenceAndUseYnCommand(
        userSequence,
        relationshipSequence,
        useYn
    );
  }

  public static Command<Mono<Mind>, MindRepository> getBySequence(
      String userSequence,
      String sequence,
      YnType useYn
  ) {
    return new MindFindOneByUserSequenceAndSequenceAndUseYnCommand(
        userSequence,
        sequence,
        useYn
    );
  }

  public static Command<Mono<Mind>, MindRepository> create(Mind mind) {
    return new MindSaveOneCommand(mind);
  }


  public static Command<Flux<Mind>, MindRepository> create(List<Mind> minds) {
    return new MindSaveAllCommand(minds);
  }

  public static Command<Mono<Mind>, MindRepository> update(Mind mind) {
    return new MindSaveOneCommand(mind);
  }

  public static Command<Flux<Mind>, MindRepository> getByUserSequence(
      String userSequence,
      YnType useYn
  ) {
    return new MindFindAllByUserSequenceAndUseYnCommand(userSequence, useYn);
  }
}
