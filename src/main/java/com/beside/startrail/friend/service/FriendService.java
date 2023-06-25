package com.beside.startrail.friend.service;

import com.beside.startrail.common.protocolbuffer.friend.FriendProtoUtil;
import com.beside.startrail.common.type.YnType;
import java.util.List;

import com.beside.startrail.friend.document.Friend;
import com.beside.startrail.friend.repository.FriendRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import protobuf.friend.FriendGetCriteriaProto;
import protobuf.friend.FriendPostProto;
import protobuf.friend.FriendPutProto;
import protobuf.friend.FriendResponseProto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class FriendService {

    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    public Mono<FriendResponseProto> getFriendBySequence(String userSequence, String sequence){
        return getVerifiedFriend(userSequence, sequence)
                .flatMap(friend -> Mono.just(FriendProtoUtil.toFriendResponseProto(friend)));
    }

    public Mono<List<FriendResponseProto>> createFriend(String userSequence, FriendPostProto friendCreateDto){
        return friendRepository.saveAll(FriendProtoUtil.postProtoToDocuments(userSequence, friendCreateDto))
                .flatMap(friend -> Mono.just(FriendProtoUtil.toFriendResponseProto(friend)))
                .collectList();
    }

    public Mono<FriendResponseProto> updateFriend(String userSequence, String sequence, FriendPutProto friendPutProto){
        return getVerifiedFriend(userSequence, sequence)
                .flatMap(friend -> Mono.just(FriendProtoUtil.updateDtoToDocument(friend, friendPutProto)))
                .flatMap(updateFrd -> friendRepository.save(updateFrd))
                .flatMap(friend -> Mono.just(FriendProtoUtil.toFriendResponseProto(friend)));
    }

    public Mono<FriendResponseProto> removeFriend(String userSequence, String sequence) {
        return getVerifiedFriend(userSequence, sequence)
                .flatMap(friend -> Mono.just(friend.notUseYn(friend)))
                .flatMap(friend -> friendRepository.save(friend))
                .flatMap(friend -> Mono.just(FriendProtoUtil.toFriendResponseProto(friend)));

    }

    public Flux<FriendResponseProto> getFriendsByCriteria(String userSequence, FriendGetCriteriaProto friendSearchCriteria){
        return friendRepository.findFriendsByCriteria(userSequence, friendSearchCriteria)
                .flatMap(friend -> Mono.just(FriendProtoUtil.toFriendResponseProto(friend)));
    }

    private Mono<Friend> getVerifiedFriend(String userSequence, String sequence){
        return friendRepository.findByUserSequenceAndSequenceAndUseYn(userSequence, sequence, YnType.Y)
                .switchIfEmpty(
                        Mono.error(
                                new IllegalArgumentException("Not Found Friend")
                        )
                );
    }
}
