package com.beside.startrail.relationship.command;

import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import reactor.core.publisher.Mono;

public class RelationshipSaveOneCommand {
  private final Relationship relationship;
  private Mono<Relationship> result;

  public RelationshipSaveOneCommand(Relationship relationship) {
    this.relationship = relationship;
  }

  public Mono<Relationship> execute(RelationshipRepository relationshipRepository) {
    result = relationshipRepository.save(relationship);

    return result;
  }
}
