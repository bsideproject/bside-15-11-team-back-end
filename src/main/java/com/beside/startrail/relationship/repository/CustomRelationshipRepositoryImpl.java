package com.beside.startrail.relationship.repository;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.model.RelationshipCountResult;
import com.beside.startrail.relationship.type.RelationshipType;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CustomRelationshipRepositoryImpl implements CustomRelationshipRepository {
  private final ReactiveMongoTemplate reactiveMongoTemplate;

  public CustomRelationshipRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
    this.reactiveMongoTemplate = reactiveMongoTemplate;
  }

  @Override
  public Mono<RelationshipCountResult> count(String userSequence, YnType useYn) {
    Aggregation aggregation = Aggregation.newAggregation(
        Aggregation.match(Criteria.where("useYn").is(useYn)),
        Aggregation.match(Criteria.where("userSequence").is(userSequence)),
        Aggregation.group()
            .count().as("total")
            .sum(ConditionalOperators.when(Criteria.where("type").is(RelationshipType.GIVEN))
                .then(1)
                .otherwise(0)).as("given")
            .sum(ConditionalOperators.when(Criteria.where("type").is(RelationshipType.TAKEN))
                .then(1)
                .otherwise(0)).as("taken")
    );

    return Mono.from(
        reactiveMongoTemplate.aggregate(
            aggregation,
            Relationship.class,
            RelationshipCountResult.class
        )
    );
  }
}
