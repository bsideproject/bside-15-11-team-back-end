package com.beside.startrail.friend.service;

import com.beside.startrail.common.type.YnType;
import java.util.List;
import java.util.stream.Collectors;

import com.beside.startrail.friend.document.Friend;
import com.beside.startrail.friend.repository.FriendRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import protobuf.friend.FriendCreateDto;
import protobuf.friend.FriendDto;
import protobuf.friend.FriendSearchCriteria;
import protobuf.friend.FriendUpdateDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class FriendService {

    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    public Mono<FriendDto> getFriendBySequence(String userSequence, String sequence){
        return getVerifiedFriend(userSequence, sequence)
                .flatMap(friend -> Mono.just(this.toResponseDto(friend)));
    }

    public Mono<List<FriendDto>> createFriend(String userSequence, FriendCreateDto friendCreateDto){
        return friendRepository.saveAll(createDtoToDocuments(userSequence, friendCreateDto))
                .flatMap(friend -> Mono.just(this.toResponseDto(friend)))
                .collectList();
    }

    public Mono<FriendDto> updateFriend(String userSequence, String sequence, FriendUpdateDto friendUpdateDto){
        return getVerifiedFriend(userSequence, sequence)
                .flatMap(friend -> Mono.just(updateDtoToDocument(friend, friendUpdateDto)))
                .flatMap(updateFrd -> friendRepository.save(updateFrd))
                .flatMap(friend -> Mono.just(this.toResponseDto(friend)));
    }

    public Mono<FriendDto> removeFriend(String userSequence, String sequence) {
        return getVerifiedFriend(userSequence, sequence)
                .flatMap(friend -> Mono.just(friend.notUseYn(friend)))
                .flatMap(friend -> friendRepository.save(friend))
                .flatMap(friend -> Mono.just(this.toResponseDto(friend)));

    }

    public Flux<FriendDto> getFriendsByCriteria(String userSequence, FriendSearchCriteria friendSearchCriteria){
        return friendRepository.findFriendsByCriteria(userSequence, friendSearchCriteria)
                .flatMap(friend -> Mono.just(this.toResponseDto(friend)));
    }

    private Mono<Friend> getVerifiedFriend(String userSequence, String sequence){
        return friendRepository.findByUserSequenceAndSequenceAndUseYn(userSequence, sequence, YnType.Y)
                .switchIfEmpty(
                        Mono.error(
                                new IllegalArgumentException("Not Found Friend")
                        )
                );
    }

    private FriendDto toResponseDto(Friend friend){
        return FriendDto.newBuilder()
                .setSequence(friend.getSequence())
                .setNickname(friend.getNickname())
                .setRelationship(friend.getRelationship())
                .setBirth(friend.getBirth())
                .setMemo(friend.getMemo())
                .setLevelInformation(friend.getLevelInformation())
                .build();
    }

    private List<Friend> createDtoToDocuments(String userSequence, FriendCreateDto friendCreateDto){
        return friendCreateDto.getNicknamesList().stream()
                .map(nickname -> Friend.builder()
                        .userSequence(userSequence)
                        .nickname(nickname)
                        .relationship(friendCreateDto.getRelationship())
                        .birth(friendCreateDto.getBirth())
                        .memo(friendCreateDto.getMemo())
                        .build()
                )
                .collect(Collectors.toList());
    }

    private Friend updateDtoToDocument(Friend friend, FriendUpdateDto friendUpdateDto){
        return Friend.builder()
            .sequence(friend.getSequence())
            .userSequence(friend.getUserSequence())
            .nickname(friendUpdateDto.getNickname())
            .relationship(friendUpdateDto.getRelationship())
            .birth(friendUpdateDto.getBirth())
            .memo(friendUpdateDto.getMemo())
            .levelInformation(friend.getLevelInformation())
            .build();
    }
}
