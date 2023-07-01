package com.beside.startrail.relationship.repository;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.model.RelationshipCountResult;
import reactor.core.publisher.Mono;

public interface CustomRelationshipRepository {
  Mono<RelationshipCountResult> count(String sequence, YnType useYn);
}
