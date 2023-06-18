package com.beside.mamgwanboo.friend;

import org.springframework.stereotype.Repository;
import protobuf.friend.FriendSearchCriteria;
import reactor.core.publisher.Flux;


@Repository
public interface CustomFriendRepository {

    Flux<Friend> findFriendsByCriteria(FriendSearchCriteria keyword);
}
