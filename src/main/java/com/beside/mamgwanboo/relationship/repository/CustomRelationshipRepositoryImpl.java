package com.beside.mamgwanboo.relationship.repository;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.relationship.document.Relationship;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import protobuf.common.type.SortOrderType;
import reactor.core.publisher.Flux;

@Repository
public class CustomRelationshipRepositoryImpl implements CustomRelationshipRepository {
  private final ReactiveMongoTemplate reactiveMongoTemplate;

  public CustomRelationshipRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
    this.reactiveMongoTemplate = reactiveMongoTemplate;
  }

  @Override
  public Flux<Relationship> findAllByFriendSequence(
      UUID friendSequence,
      SortOrderType sortOrderType
  ) {
    Query query = new Query(
        Criteria
            .where("friendSequence").is(friendSequence)
            .andOperator(
                Criteria.where("useYn").is(YnType.Y)
            )
    ).with(
        getSort("date", sortOrderType)
    );

    return reactiveMongoTemplate.find(query, Relationship.class);
  }

  private Sort getSort(String field, SortOrderType sortOrderType) {
    Sort sort = Sort.by(field);

    switch (sortOrderType) {
      case ASC -> {
        sort.ascending();
      }
      case DESC -> {
        sort.descending();
      }
      default -> {
        sort.descending();
      }
    }

    return sort;
  }
}
