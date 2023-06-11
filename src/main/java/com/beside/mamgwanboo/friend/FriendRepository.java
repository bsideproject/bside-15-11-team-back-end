package com.beside.mamgwanboo.friend;

import com.beside.mamgwanboo.common.type.YnType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface FriendRepository extends ReactiveMongoRepository<Friend, UUID> {
    Mono<Friend> findBySequenceAndUseYn(String id, YnType useYn);
    Flux<Friend> findByUseYn(YnType useYn);
}
