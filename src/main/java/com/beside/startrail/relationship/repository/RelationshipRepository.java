package com.beside.startrail.relationship.repository;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.document.Relationship;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RelationshipRepository extends ReactiveMongoRepository<Relationship, UUID> {
  Flux<Relationship> findAllByUserSequenceAndFriendSequenceAndUseYn(
      String userSequence,
      String friendSequence,
      YnType useYn,
      Sort sort
  );

  Flux<Relationship> findAllByUserSequenceAndFriendSequenceAndUseYn(
      String userSequence,
      String friendSequence,
      YnType useYn
  );

  Mono<Relationship> findOneByUserSequenceAndSequenceAndUseYn(
      String userSequence,
      String sequence,
      YnType useYn
  );

  Mono<Relationship> deleteByUserSequenceAndSequenceAndUseYn(
      String userSequence,
      String sequence,
      YnType useYn
  );

  Flux<Relationship> findByUserSequenceAndUseYn(String userSequence, YnType useYn);
}
