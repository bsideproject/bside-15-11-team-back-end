package com.beside.startrail.relationship.repository;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.document.Relationship;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class CustomRelationshipRepositoryImpl implements CustomRelationshipRepository {
  private final String USER_SEQUENCE_FIELD = "userSequence";
  private final String NICK_NAME_FIELD = "nickname";
  private final ReactiveMongoTemplate reactiveMongoTemplate;

  public CustomRelationshipRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
    this.reactiveMongoTemplate = reactiveMongoTemplate;
  }

  @Override
  public Flux<Relationship> findAllRelationshipByUserSequenceAndNicknameKeywordAndUseYn(
      String userSequence,
      String nicknameKeyword,
      YnType useYn
  ) {
    String keywordReg = ".*" + nicknameKeyword + ".*";

    Query query = new Query(
        Criteria.where("useYn").is(useYn)
            .andOperator(Criteria.where(USER_SEQUENCE_FIELD).is(userSequence))
            .orOperator(Criteria.where(NICK_NAME_FIELD).regex(keywordReg))
    );

    return reactiveMongoTemplate.find(query, Relationship.class);
  }
}
