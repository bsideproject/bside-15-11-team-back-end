package com.beside.startrail.user.repository;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.document.UserId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import protobuf.common.type.OauthServiceType;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, UserId> {
  Mono<User> findUserByUserId_OauthServiceType_AndUserId_ServiceUserIdAndUseYn(
      OauthServiceType oauthServiceType,
      String serviceUserId,
      YnType useYn
  );

  Mono<Boolean> existsBySequenceAndUseYn(String sequence, YnType useYn);
}
