package com.beside.mamgwanboo.friend;

import com.beside.mamgwanboo.common.type.YnType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import protobuf.friend.FriendCreateDto;
import protobuf.friend.FriendDto;
import protobuf.friend.FriendUpdateDto;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    public Mono<List<FriendDto>> findFriendAll(){
        return friendRepository.findByUseYn(YnType.Y)
                .flatMap(friend -> Mono.just(this.toResponseDto(friend)))
                .collectList();
    }
    public Mono<FriendDto> findFriend(String sequence){
        return findVerifiedFriend(sequence)
                .flatMap(friend -> Mono.just(this.toResponseDto(friend)));
    }

    public Mono<List<FriendDto>> createFriend(FriendCreateDto friendCreateDto){
        return friendRepository.saveAll(createDtoToDocuments(friendCreateDto))
                .flatMap(friend -> Mono.just(this.toResponseDto(friend)))
                .collectList();
    }

    public Mono<FriendDto> updateFriend(String sequence, FriendUpdateDto friendUpdateDto){
        return findVerifiedFriend(sequence)
                .flatMap(friend -> Mono.just(updateDtoToDocument(friend, friendUpdateDto)))
                .flatMap(updateFrd -> friendRepository.save(updateFrd))
                .flatMap(friend -> Mono.just(this.toResponseDto(friend)));
    }

    public Mono<FriendDto> deleteFriend(String sequence) {
        return findVerifiedFriend(sequence)
                .flatMap(friend -> Mono.just(friend.notUseYn(friend)))
                .flatMap(friend -> friendRepository.save(friend))
                .flatMap(friend -> Mono.just(this.toResponseDto(friend)));

    }

    private Mono<Friend> findVerifiedFriend(String sequence){
        return friendRepository.findBySequenceAndUseYn(sequence, YnType.Y)
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

    private List<Friend> createDtoToDocuments(FriendCreateDto friendCreateDto){
        return friendCreateDto.getNicknamesList().stream()
                .map(nickname -> Friend.builder()
                        .userSequence("TEST")
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
