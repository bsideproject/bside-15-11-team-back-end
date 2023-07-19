package com.beside.startrail.relationLevel.repository;

import com.beside.startrail.relationLevel.document.RelationLevel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RelationLevelRepository extends ReactiveMongoRepository<RelationLevel, String> {
  Mono<RelationLevel> findByCountFromIsLessThanEqualAndCountToIsGreaterThanEqual(
      int countFrom,
      int countTo
  );
}