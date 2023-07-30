package com.beside.startrail.relationshiplevel.repository;

import com.beside.startrail.relationshiplevel.document.RelationshipLevel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RelationshipLevelRepository extends ReactiveMongoRepository<RelationshipLevel, String> {
  Mono<RelationshipLevel> findByCountFromIsLessThanEqualAndCountToIsGreaterThanEqual(
      int countFrom,
      int countTo
  );
}