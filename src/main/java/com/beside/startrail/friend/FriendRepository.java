package com.beside.startrail.friend;

import com.beside.startrail.common.type.YnType;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FriendRepository extends ReactiveMongoRepository<Friend, UUID>, CustomFriendRepository {
    Mono<Friend> findBySequenceAndUseYn(String id, YnType useYn);
    Flux<Friend> findByUseYn(YnType useYn);
}
