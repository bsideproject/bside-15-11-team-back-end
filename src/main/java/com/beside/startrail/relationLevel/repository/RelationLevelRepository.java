package com.beside.startrail.relationLevel.repository;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationLevel.document.RelationLevel;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RelationLevelRepository extends ReactiveMongoRepository<RelationLevel, String> {
    Mono<RelationLevel> findBySequenceAndUseYn(String sequence, YnType ynType);
    Mono<RelationLevel> findByLevel(Integer level);
    Flux<RelationLevel> findAllByUseYn(YnType ynType, Sort sort);
}
