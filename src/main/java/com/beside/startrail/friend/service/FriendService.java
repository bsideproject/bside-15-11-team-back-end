package com.beside.startrail.friend.service;

import com.beside.startrail.common.protocolbuffer.friend.FriendProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.friend.document.Friend;
import com.beside.startrail.friend.repository.FriendRepository;
import com.beside.startrail.relationLevel.document.RelationLevel;
import com.beside.startrail.relationLevel.repository.RelationLevelRepository;
import com.beside.startrail.relationship.model.RelationshipCountResult;
import com.beside.startrail.relationship.repository.CustomRelationshipRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import protobuf.common.LevelInformationProto;
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
  private final RelationLevelRepository relationLevelRepository;
  private final CustomRelationshipRepository customRelationshipRepository;

  public FriendService(
      FriendRepository friendRepository,
      RelationLevelRepository relationLevelRepository,
      CustomRelationshipRepository customRelationshipRepository
  ) {
    this.friendRepository = friendRepository;
    this.relationLevelRepository = relationLevelRepository;
    this.customRelationshipRepository = customRelationshipRepository;
  }

  public Mono<FriendResponseProto> getFriendBySequence(String userSequence, String sequence) {
    return getVerifiedFriend(userSequence, sequence)
        .flatMap(friend -> getFriendLevelInfo(userSequence, friend.getSequence())
            .map(levelInformation ->
                setLevelInfo(FriendProtoUtil.toFriendResponseProto(friend), levelInformation)
            )
        );
  }

  public Mono<List<FriendResponseProto>> createFriend(
      String userSequence,
      FriendPostProto friendCreateDto
  ) {
    return friendRepository.saveAll(
            FriendProtoUtil.postProtoToDocuments(userSequence, friendCreateDto))
        .flatMap(friend -> Mono.just(FriendProtoUtil.toFriendResponseProto(friend)))
        .collectList();
  }

  public Mono<FriendResponseProto> updateFriend(
      String userSequence, String sequence,
      FriendPutProto friendPutProto
  ) {
    return getVerifiedFriend(userSequence, sequence)
        .flatMap(friend -> Mono.just(FriendProtoUtil.updateDtoToDocument(friend, friendPutProto)))
        .flatMap(friendRepository::save)
        .flatMap(friend -> getFriendLevelInfo(userSequence, friend.getSequence())
            .map(levelInformation ->
                setLevelInfo(FriendProtoUtil.toFriendResponseProto(friend), levelInformation)
            )
        );
  }

  public Mono<FriendResponseProto> removeFriend(String userSequence, String sequence) {
    return getVerifiedFriend(userSequence, sequence)
        .flatMap(friend -> Mono.just(Friend.from(friend, YnType.N)))
        .flatMap(friendRepository::save)
        .map(FriendProtoUtil::toFriendResponseProto);

  }

  public Flux<FriendResponseProto> getFriendsByCriteria(
      String userSequence,
      FriendGetCriteriaProto friendSearchCriteria
  ) {
    return friendRepository.findFriendsByCriteria(userSequence, friendSearchCriteria)
        .flatMap(friend ->
            getFriendLevelInfo(userSequence, friend.getSequence())
                .map(levelInformation ->
                    setLevelInfo(FriendProtoUtil.toFriendResponseProto(friend), levelInformation)
                )
        );
  }

  private Mono<Friend> getVerifiedFriend(String userSequence, String sequence) {
    return friendRepository.findByUserSequenceAndSequenceAndUseYn(userSequence, sequence, YnType.Y)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Not Found Friend")));
  }

  private Mono<LevelInformationProto> getFriendLevelInfo(
      String userSequence,
      String friendSequence
  ) {
    return customRelationshipRepository.countByFriend(userSequence, friendSequence, YnType.Y)
        .switchIfEmpty(Mono.just(RelationshipCountResult.builder().build()))
        .flatMap(relationshipCountResult ->
            getRelationLevelByCount(relationshipCountResult.getTotal())
                .map(relationLevel ->
                    toLevelInformationProto(
                        relationshipCountResult,
                        relationLevel
                    )
                )
        );
  }

  private Mono<RelationLevel> getRelationLevelByCount(Integer count) {
    return relationLevelRepository.findByCountFromIsLessThanEqualAndCountToIsGreaterThanEqual(
            count,
            count
        )
        .switchIfEmpty(Mono.just(RelationLevel.makeDefault()));
  }

  private LevelInformationProto toLevelInformationProto(
      RelationshipCountResult relationshipCountResult,
      RelationLevel relationLevel
  ) {
    return LevelInformationProto.newBuilder()
        .setTotal(relationshipCountResult.getTotal())
        .setGiven(relationshipCountResult.getGiven())
        .setTaken(relationshipCountResult.getTaken())
        .setLevel(relationLevel.getLevel())
        .setTitle(relationLevel.getTitle())
        .setDescription(relationLevel.getDescription())
        .build();
  }

  private FriendResponseProto setLevelInfo(
      FriendResponseProto friendResponseProto,
      LevelInformationProto levelInformationProto
  ) {
    return friendResponseProto.toBuilder()
        .setLevelInformation(levelInformationProto)
        .build();
  }
}
