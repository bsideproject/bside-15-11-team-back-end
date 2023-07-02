package com.beside.startrail.relationLevel.repository;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationLevel.document.RelationLevel;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import protobuf.common.RelationLevelGetCriteriaProto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CustomRelationLevelRepositoryImpl implements CustomRelationLevelRepository{

    private final String SEQUENCE_FIELD = "sequence";
    private final String LEVEL_FIELD = "level";
    private final String COUNT_FIELD = "count";
    private final String COUNT_FROM_FIELD = "countFrom";
    private final String COUNT_TO_FIELD = "countTo";

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public CustomRelationLevelRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Flux<RelationLevel> findRelationLevelByCriteria(RelationLevelGetCriteriaProto criteriaProto) {
        return Mono.just(
                    new Query(Criteria.where("useYn").is(YnType.Y))
                            .addCriteria(makeQueryCriteria(criteriaProto)))
                .flatMapMany(query -> reactiveMongoTemplate.find(query, RelationLevel.class));
    }

    private Criteria makeQueryCriteria(RelationLevelGetCriteriaProto criteriaProto){
        switch (criteriaProto.getSearchKey()){
            case SEQUENCE_FIELD :
                return Criteria.where(SEQUENCE_FIELD).is(criteriaProto.getSearchValue());
            case LEVEL_FIELD:
                return Criteria.where(LEVEL_FIELD).is(Integer.valueOf(criteriaProto.getSearchValue()));
            case COUNT_FIELD:
                return Criteria.where(COUNT_FROM_FIELD).lte(Integer.valueOf(criteriaProto.getSearchValue()))
                        .andOperator(Criteria.where(COUNT_TO_FIELD).gte(Integer.valueOf(criteriaProto.getSearchValue())));
            default:
                return Criteria.where("");
        }
    }
}
