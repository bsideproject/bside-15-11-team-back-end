package com.beside.startrail.friend.repository;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.friend.document.Friend;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

public class CustomFriendRepositoryImpl implements CustomFriendRepository {

  private final String USER_SEQUENCE_FIELD = "userSequence";
  private final String NICK_NAME_FIELD = "nickname";
  private final ReactiveMongoTemplate reactiveMongoTemplate;

  public CustomFriendRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
    this.reactiveMongoTemplate = reactiveMongoTemplate;
  }

  @Override
  public Flux<Friend> findFriendsByCriteria(
      String userSequence,
      String nicknameKeyword
  ) {
    String keywordReg = ".*" + nicknameKeyword + ".*";

    Query query = new Query(
        Criteria.where("useYn").is(YnType.Y)
            .andOperator(Criteria.where(USER_SEQUENCE_FIELD).is(userSequence))
            .orOperator(Criteria.where(NICK_NAME_FIELD).regex(keywordReg))
    );

    return reactiveMongoTemplate.find(query, Friend.class);
  }
}
