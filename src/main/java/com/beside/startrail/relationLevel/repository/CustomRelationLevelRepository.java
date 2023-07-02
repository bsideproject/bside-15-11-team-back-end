package com.beside.startrail.relationLevel.repository;

import com.beside.startrail.relationLevel.document.RelationLevel;
import org.springframework.stereotype.Repository;
import protobuf.common.RelationLevelGetCriteriaProto;
import reactor.core.publisher.Flux;

@Repository
public interface CustomRelationLevelRepository {

    Flux<RelationLevel> findRelationLevelByCriteria(RelationLevelGetCriteriaProto criteriaProto);
}
