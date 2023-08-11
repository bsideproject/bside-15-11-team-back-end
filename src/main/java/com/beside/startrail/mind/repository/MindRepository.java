package com.beside.startrail.mind.repository;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.document.Mind;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MindRepository extends ReactiveMongoRepository<Mind, UUID> {
  Mono<Integer> countByRelationshipSequenceAndUseYn(
      String relationshipSequence,
      YnType useYn
  );

  Flux<Mind> findAllByRelationshipSequenceAndUseYn(
      String relationshipSequence,
      YnType useYn,
      Sort sort
  );

  Flux<Mind> findAllByRelationshipSequenceAndUseYn(
      String relationshipSequence,
      YnType useYn
  );

  Mono<Mind> findOneBySequenceAndUseYn(
      String sequence,
      YnType useYn
  );

  Mono<Mind> deleteByUserSequenceAndSequenceAndUseYn(
      String userSequence,
      String sequence,
      YnType useYn
  );

  Flux<Mind> findByUserSequenceAndUseYn(String userSequence, YnType useYn);
}
