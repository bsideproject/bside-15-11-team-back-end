package com.beside.startrail.relationship.command;

import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import java.util.List;
import reactor.core.publisher.Flux;

public class RelationshipSaveAllCommand {
  private final List<Relationship> relationships;
  private Flux<Relationship> result;

  public RelationshipSaveAllCommand(List<Relationship> relationships) {
    this.relationships = relationships;
  }

  public Flux<Relationship> execute(RelationshipRepository relationshipRepository) {
    result = relationshipRepository.saveAll(relationships);

    return result;
  }
}
