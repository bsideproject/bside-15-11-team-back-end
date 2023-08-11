package com.beside.startrail.relationship.service;

import com.beside.startrail.common.protocolbuffer.relationship.RelationshipProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.mind.model.MindCountResult;
import com.beside.startrail.mind.repository.CustomMindRepository;
import com.beside.startrail.mind.repository.MindRepository;
import com.beside.startrail.relationship.command.RelationshipFindAllByUserSequenceAndNicknameKeywordAndUseYnCommand;
import com.beside.startrail.relationship.command.RelationshipFindOneByUserSequenceAndSequenceAndUseYnCommand;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.repository.RelationshipRepository;
import com.beside.startrail.relationshiplevel.document.RelationshipLevel;
import com.beside.startrail.relationshiplevel.repository.RelationshipLevelRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import protobuf.common.LevelInformationProto;
import protobuf.relationship.RelationshipPostRequestProto;
import protobuf.relationship.RelationshipPutRequestProto;
import protobuf.relationship.RelationshipResponseProto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RelationshipService {
  private final RelationshipRepository relationshipRepository;
  private final RelationshipLevelRepository relationshipLevelRepository;
  private final CustomMindRepository customMindRepository;
  private final MindRepository mindRepository;

  public RelationshipService(
      RelationshipRepository relationshipRepository,
      RelationshipLevelRepository relationshipLevelRepository,
      CustomMindRepository customMindRepository,
      MindRepository mindRepository
  ) {
    this.relationshipRepository = relationshipRepository;
    this.relationshipLevelRepository = relationshipLevelRepository;
    this.customMindRepository = customMindRepository;
    this.mindRepository = mindRepository;
  }

  public Mono<RelationshipResponseProto> getBySequence(String userSequence, String sequence) {
    return getVerified(userSequence, sequence)
        .flatMap(relationship -> getLevelInformation(relationship.getSequence())
            .map(levelInformation ->
                setLevelInformation(RelationshipProtoUtil.toRelationshipResponseProto(relationship),
                    levelInformation)
            )
        );
  }

  public Mono<List<RelationshipResponseProto>> create(
      String userSequence,
      RelationshipPostRequestProto relationshipPostRequestProto
  ) {
    List<Relationship> relationships =
        RelationshipProtoUtil.toRelationships(userSequence, relationshipPostRequestProto);

    if (Objects.isNull(relationships)) {
      return Mono.just(List.of());
    }

    return relationshipRepository.saveAll(relationships)
        .flatMap(friend -> Mono.just(RelationshipProtoUtil.toRelationshipResponseProto(friend)))
        .collectList();
  }

  public Mono<RelationshipResponseProto> update(
      String userSequence,
      String sequence,
      RelationshipPutRequestProto relationshipPutRequestProto
  ) {
    return getVerified(userSequence, sequence)
        .flatMap(relationship -> Mono.justOrEmpty(
            RelationshipProtoUtil.toRelationships(relationship, relationshipPutRequestProto)))
        .flatMap(relationshipRepository::save)
        .flatMap(relationship -> getLevelInformation(relationship.getSequence())
            .map(levelInformation ->
                setLevelInformation(RelationshipProtoUtil.toRelationshipResponseProto(relationship),
                    levelInformation)
            )
        );
  }

  public Flux<RelationshipResponseProto> get(
      String userSequence,
      String nicknameKeyword,
      String sort
  ) {
    Flux<RelationshipResponseProto> relationshipResponseProtos =
        relationshipRepository.findRelationshipsByCriteria(userSequence, nicknameKeyword)
            .flatMap(relationship ->
                getLevelInformation(relationship.getSequence())
                    .map(levelInformation ->
                        setLevelInformation(
                            RelationshipProtoUtil.toRelationshipResponseProto(relationship),
                            levelInformation)
                    )
            );

    return sort(relationshipResponseProtos, sort);
  }

  private Mono<Relationship> getVerified(String userSequence, String sequence) {
    return relationshipRepository.findByUserSequenceAndSequenceAndUseYn(userSequence, sequence,
            YnType.Y)
        .switchIfEmpty(Mono.error(new IllegalArgumentException("Not Found Relationship")));
  }

  private Mono<LevelInformationProto> getLevelInformation(String relationshipSequence) {
    return customMindRepository.countBySequenceAndUseYn(relationshipSequence, YnType.Y)
        .switchIfEmpty(Mono.just(MindCountResult.builder().build()))
        .flatMap(mindCountResult ->
            getRelationshipLevelByCount(mindCountResult.getTotal())
                .map(relationLevel ->
                    toLevelInformationProto(
                        mindCountResult,
                        relationLevel
                    )
                )
        );
  }

  private Mono<RelationshipLevel> getRelationshipLevelByCount(int count) {
    return relationshipLevelRepository.findByCountFromIsLessThanEqualAndCountToIsGreaterThanEqual(
            count,
            count
        )
        .switchIfEmpty(Mono.just(RelationshipLevel.makeDefault()));
  }

  private LevelInformationProto toLevelInformationProto(
      MindCountResult relationshipCountResult,
      RelationshipLevel relationshipLevel
  ) {
    LevelInformationProto.Builder builder = LevelInformationProto
        .newBuilder()
        .clear()
        .setTotal(relationshipCountResult.getTotal())
        .setGiven(relationshipCountResult.getGiven())
        .setTaken(relationshipCountResult.getTaken())
        .setLevel(relationshipLevel.getLevel());

    if (Objects.nonNull(relationshipLevel.getTitle())) {
      builder.setTitle(relationshipLevel.getTitle());
    }
    if (Objects.nonNull(relationshipLevel.getDescription())) {
      builder.setDescription(relationshipLevel.getDescription());
    }

    return builder.build();
  }

  private RelationshipResponseProto setLevelInformation(
      RelationshipResponseProto relationshipResponseProto,
      LevelInformationProto levelInformationProto
  ) {
    return relationshipResponseProto.toBuilder()
        .setLevelInformation(levelInformationProto)
        .build();
  }

  private static Flux<RelationshipResponseProto> sort(
      Flux<RelationshipResponseProto> relationshipResponseProtos,
      String sort
  ) {
    switch (sort) {
      case "level" -> {
        return relationshipResponseProtos
            .sort(
                Comparator.<RelationshipResponseProto, Integer>comparing(
                        relationshipResponseProto ->
                            Optional.ofNullable(relationshipResponseProto)
                                .map(RelationshipResponseProto::getLevelInformation)
                                .map(LevelInformationProto::getLevel)
                                .orElse(0)
                    )
                    .reversed()
            );
      }
      default -> {
        return relationshipResponseProtos
            .sort(
                Comparator.comparing(relationshipResponseProto ->
                    Optional.ofNullable(relationshipResponseProto)
                        .map(RelationshipResponseProto::getNickname)
                        .orElse("")
                )
            );
      }
    }
  }

  public static RelationshipFindOneByUserSequenceAndSequenceAndUseYnCommand getByUserSequenceAndSequenceAndUseYn(
      String userSequence,
      String sequence,
      YnType useYn
  ) {
    return new RelationshipFindOneByUserSequenceAndSequenceAndUseYnCommand(
        userSequence,
        sequence,
        useYn
    );
  }

  public static RelationshipFindAllByUserSequenceAndNicknameKeywordAndUseYnCommand getByUserSequenceAndNicknameKeywordAndUseYn(
      String userSequence,
      String nicknameKeyword,
      YnType useYn
  ) {
    return new RelationshipFindAllByUserSequenceAndNicknameKeywordAndUseYnCommand(
        userSequence,
        nicknameKeyword,
        useYn
    );
  }
}
