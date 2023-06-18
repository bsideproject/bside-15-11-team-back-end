package com.beside.mamgwanboo.relationship.repository;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.relationship.document.Relationship;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RelationshipRepository extends ReactiveMongoRepository<Relationship, UUID> {
  Mono<Integer> countAllByUserSequenceAndUseYn(UUID userSequence, YnType useYn);

  Mono<Relationship> findOneBySequenceAndUseYn(UUID sequence, YnType useYn);
}
