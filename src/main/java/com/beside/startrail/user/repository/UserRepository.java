package com.beside.startrail.user.repository;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.document.UserId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, UserId> {
  Mono<User> findUserByUserIdAndUseYn(
      UserId userId,
      YnType useYn
  );
}
