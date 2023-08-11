package com.beside.startrail.mind.repository;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.document.Mind;
import com.beside.startrail.mind.model.MindCountResult;
import com.beside.startrail.mind.type.MindType;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CustomMindRepositoryImpl implements CustomMindRepository {
  private final ReactiveMongoTemplate reactiveMongoTemplate;

  public CustomMindRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
    this.reactiveMongoTemplate = reactiveMongoTemplate;
  }

  @Override
  public Mono<MindCountResult> countByUserSequenceAndUseYn(
      String userSequence,
      YnType useYn
  ) {
    Aggregation aggregation = Aggregation.newAggregation(
        Aggregation.match(Criteria.where("useYn").is(useYn)),
        Aggregation.match(Criteria.where("userSequence").is(userSequence)),
        Aggregation.group()
            .count().as("total")
            .sum(ConditionalOperators.when(Criteria.where("type").is(MindType.GIVEN))
                .then(1)
                .otherwise(0)).as("given")
            .sum(ConditionalOperators.when(Criteria.where("type").is(MindType.TAKEN))
                .then(1)
                .otherwise(0)).as("taken")
    );

    return Mono.from(
        reactiveMongoTemplate.aggregate(
            aggregation,
            Mind.class,
            MindCountResult.class
        )
    );
  }

  @Override
  public Mono<MindCountResult> countByRelationshipSequenceAndUseYn(
      String userSequence,
      String relationshipSequence,
      YnType useYn
  ) {
    Aggregation aggregation = Aggregation.newAggregation(
        Aggregation.match(Criteria.where("useYn").is(useYn)),
        Aggregation.match(Criteria.where("userSequence").is(userSequence)),
        Aggregation.match(Criteria.where("relationshipSequence").is(relationshipSequence)),
        Aggregation.group()
            .count().as("total")
            .sum(ConditionalOperators.when(Criteria.where("type").is(MindType.GIVEN))
                .then(1)
                .otherwise(0)).as("given")
            .sum(ConditionalOperators.when(Criteria.where("type").is(MindType.TAKEN))
                .then(1)
                .otherwise(0)).as("taken")
    );

    return Mono.from(
        reactiveMongoTemplate.aggregate(
            aggregation,
            Mind.class,
            MindCountResult.class
        )
    );
  }
}
