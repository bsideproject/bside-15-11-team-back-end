package com.beside.startrail.friend.service;

import com.beside.startrail.common.protocolbuffer.friend.FriendProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.friend.document.Friend;
import com.beside.startrail.friend.repository.FriendRepository;
import com.beside.startrail.relationLevel.document.RelationLevel;
import com.beside.startrail.relationLevel.repository.RelationLevelRepository;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.model.RelationshipCountResult;
import com.beside.startrail.relationship.repository.CustomRelationshipRepository;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import protobuf.common.LevelInformationProto;
import protobuf.friend.FriendPostRequestProto;
import protobuf.friend.FriendPutRequestProto;
import protobuf.friend.FriendResponseProto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class FriendService {

  private final FriendRepository friendRepository;
  private final RelationLevelRepository relationLevelRepository;
  private final CustomRelationshipRepository customRelationshipRepository;
  private final RelationshipRepository relationshipRepository;

  public FriendService(
      FriendRepository friendRepository,
      RelationLevelRepository relationLevelRepository,
      CustomRelationshipRepository customRelationshipRepository,
      RelationshipRepository relationshipRepository
  ) {
    this.friendRepository = friendRepository;
    this.relationLevelRepository = relationLevelRepository;
    this.customRelationshipRepository = customRelationshipRepository;
    this.relationshipRepository = relationshipRepository;
  }

  public Mono<FriendResponseProto> getFriendBySequence(String userSequence, String sequence) {
    return getVerifiedFriend(userSequence, sequence)
        .flatMap(friend -> getFriendLevelInfo(friend.getSequence())
            .map(levelInformation ->
                setLevelInformation(FriendProtoUtil.toFriendResponseProto(friend), levelInformation)
            )
        );
  }

  public Mono<List<FriendResponseProto>> createFriend(
      String userSequence,
      FriendPostRequestProto friendPostRequestProto
  ) {
    List<Friend> friends = FriendProtoUtil.toFriends(userSequence, friendPostRequestProto);

    if (Objects.isNull(friends)) {
      return Mono.just(List.of());
    }

    return friendRepository.saveAll(friends)
        .flatMap(friend -> Mono.just(FriendProtoUtil.toFriendResponseProto(friend)))
        .collectList();
  }

  public Mono<FriendResponseProto> updateFriend(
      String userSequence, String sequence,
      FriendPutRequestProto friendPutRequestProto
  ) {
    return getVerifiedFriend(userSequence, sequence)
        .flatMap(friend -> Mono.justOrEmpty(FriendProtoUtil.toFriends(friend, friendPutRequestProto)))
        .flatMap(friendRepository::save)
        .flatMap(friend -> getFriendLevelInfo(friend.getSequence())
            .map(levelInformation ->
                setLevelInformation(FriendProtoUtil.toFriendResponseProto(friend), levelInformation)
            )
        );
  }

  public Mono<Void> removeFriend(String userSequence, String sequence) {
    return Mono.when(
        getVerifiedFriend(userSequence, sequence)
            .map(friend -> Friend.from(friend, YnType.N))
            .flatMap(friendRepository::save),
        relationshipRepository.findAllByFriendSequenceAndUseYn(
                sequence,
                YnType.Y
            )
            .flatMap(relationship ->
                relationshipRepository.save(Relationship.from(
                    relationship,
                    YnType.N
                ))
            )
    );
  }

  public Flux<FriendResponseProto> getFriends(
      String userSequence,
      String nicknameKeyword,
      String sort
  ) {
    Flux<FriendResponseProto> friendResponseProtos =
        friendRepository.findFriendsByCriteria(userSequence, nicknameKeyword)
            .flatMap(friend ->
                getFriendLevelInfo(friend.getSequence())
                    .map(levelInformation ->
                        setLevelInformation(FriendProtoUtil.toFriendResponseProto(friend),
                            levelInformation)
                    )
            );

    return sort(friendResponseProtos, sort);
  }

  private Mono<Friend> getVerifiedFriend(String userSequence, String sequence) {
    return friendRepository.findByUserSequenceAndSequenceAndUseYn(userSequence, sequence, YnType.Y)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Not Found Friend")));
  }

  private Mono<LevelInformationProto> getFriendLevelInfo(String friendSequence) {
    return customRelationshipRepository.countByFriendSequenceAndUseYn(friendSequence, YnType.Y)
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
    LevelInformationProto.Builder builder = LevelInformationProto
        .newBuilder()
        .clear()
        .setTotal(relationshipCountResult.getTotal())
        .setGiven(relationshipCountResult.getGiven())
        .setTaken(relationshipCountResult.getTaken())
        .setLevel(relationLevel.getLevel());

    if (Objects.nonNull(relationLevel.getTitle())) {
      builder.setTitle(relationLevel.getTitle());
    }
    if (Objects.nonNull(relationLevel.getDescription())) {
      builder.setDescription(relationLevel.getDescription());
    }

    return builder.build();
  }

  private FriendResponseProto setLevelInformation(
      FriendResponseProto friendResponseProto,
      LevelInformationProto levelInformationProto
  ) {
    return friendResponseProto.toBuilder()
        .setLevelInformation(levelInformationProto)
        .build();
  }

  private Flux<FriendResponseProto> sort(
      Flux<FriendResponseProto> friendResponseProtos,
      String sort
  ) {
    switch (sort) {
      case "level" -> {
        return friendResponseProtos
            .sort(
                Comparator.<FriendResponseProto, Integer>comparing(friendResponseProto ->
                        Optional.ofNullable(friendResponseProto)
                            .map(FriendResponseProto::getLevelInformation)
                            .map(LevelInformationProto::getLevel)
                            .orElse(0)
                    )
                    .reversed()
            );
      }
      default -> {
        return friendResponseProtos
            .sort(
                Comparator.comparing(friendResponseProto ->
                    Optional.ofNullable(friendResponseProto)
                        .map(FriendResponseProto::getNickname)
                        .orElse("")
                )
            );
      }
    }
  }
}
