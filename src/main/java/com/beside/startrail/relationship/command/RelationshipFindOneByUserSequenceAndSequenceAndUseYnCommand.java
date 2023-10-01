package com.beside.startrail.relationship.command;

import com.beside.startrail.common.command.Command;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import reactor.core.publisher.Mono;

public class RelationshipFindOneByUserSequenceAndSequenceAndUseYnCommand
    implements Command<Mono<Relationship>, RelationshipRepository> {
  private final String userSequence;
  private final String sequence;
  private final YnType useYn;

  private Mono<Relationship> result;

  public RelationshipFindOneByUserSequenceAndSequenceAndUseYnCommand(
      String userSequence,
      String sequence,
      YnType useYn
  ) {
    this.userSequence = userSequence;
    this.sequence = sequence;
    this.useYn = useYn;
  }

  @Override
  public Mono<Relationship> execute(RelationshipRepository relationshipRepository) {
    result = relationshipRepository.findByUserSequenceAndSequenceAndUseYn(
        userSequence,
        sequence,
        useYn
    );

    return result;
  }
}
