package com.beside.mamgwanboo.relationship.command;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.relationship.document.Relationship;
import com.beside.mamgwanboo.relationship.repository.RelationshipRepository;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class RelationshipFindOneBySequenceCommand {
  private final UUID sequence;
  private Mono<Relationship> result;

  public RelationshipFindOneBySequenceCommand(UUID sequence) {
    this.sequence = sequence;
  }

  public Mono<Relationship> execute(RelationshipRepository relationshipRepository) {
    result = relationshipRepository.findOneBySequenceAndUseYn(sequence, YnType.Y);

    return result;
  }
}
