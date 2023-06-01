package com.beside.mamgwanboo.user.repository;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.user.document.User;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import protobuf.common.type.OauthServiceType;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, UUID> {
  Mono<User> findUserByUserInformation_OauthServiceTypeAndUserInformation_ServiceUserIdAndUseYn(
      OauthServiceType oauthServiceType, String serviceUserId, YnType useYn);
}
