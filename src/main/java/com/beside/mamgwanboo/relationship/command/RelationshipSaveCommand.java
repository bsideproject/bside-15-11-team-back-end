package com.beside.mamgwanboo.relationship.command;

import com.beside.mamgwanboo.relationship.document.Relationship;
import com.beside.mamgwanboo.relationship.repository.RelationshipRepository;
import java.util.List;
import reactor.core.publisher.Flux;

public class RelationshipSaveCommand {
  private final List<Relationship> relationships;
  private Flux<Relationship> result;

  public RelationshipSaveCommand(List<Relationship> relationships) {
    this.relationships = relationships;
  }

  public Flux<Relationship> execute(RelationshipRepository relationshipRepository) {
    result = relationshipRepository.saveAll(relationships);

    return result;
  }
}
