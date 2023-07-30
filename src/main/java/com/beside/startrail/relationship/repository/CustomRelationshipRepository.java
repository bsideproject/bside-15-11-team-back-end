package com.beside.startrail.relationship.repository;

import com.beside.startrail.relationship.document.Relationship;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface CustomRelationshipRepository {

  Flux<Relationship> findRelationshipsByCriteria(
      String userSequence,
      String nicknameKeyword
  );
}
