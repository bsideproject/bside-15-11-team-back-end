package com.beside.mamgwanboo.relationship.repository;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.relationship.document.Relationship;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RelationshipRepository extends ReactiveMongoRepository<Relationship, UUID> {
  Mono<Long> countAllByUserSequenceAndUseYn(String userSequence, YnType useYn);

  Flux<Relationship> findAllByUserSequenceAndFriendSequenceAndUseYn(
      String userSequence,
      String friendSequence,
      YnType useYn,
      Sort sort
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
}
