package com.beside.startrail.friend.repository;

import com.beside.startrail.common.type.YnType;
import java.util.UUID;

import com.beside.startrail.friend.document.Friend;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FriendRepository extends ReactiveMongoRepository<Friend, UUID>, CustomFriendRepository {
    Mono<Friend> findByUserSequenceAndSequenceAndUseYn(String userSequence, String sequence, YnType useYn);
}
