package com.beside.startrail.relationship.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.beside.startrail.relationship.type.SortType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import protobuf.common.LevelInformationProto;
import protobuf.relationship.RelationshipResponseProto;
import reactor.core.publisher.Flux;

public class RelationshipServiceTest {
  @ParameterizedTest
  @MethodSource("provideSortParameterAndResult")
  void sort(SortParameterAndResult sortParameterAndResult) {
    // given
    Flux<RelationshipResponseProto> relationshipResponseProtos =
        sortParameterAndResult.relationshipResponseProtos;
    SortType sortType = sortParameterAndResult.sortType;
    Flux<RelationshipResponseProto> sortedRelationshipResponseProtos =
        sortParameterAndResult.sortedRelationshipResponseProtos;

    // when
    Flux<RelationshipResponseProto> result =
        RelationshipService.sort(relationshipResponseProtos, sortType);

    // then
    assertThat(result.collectList().block()).isEqualTo(
        sortedRelationshipResponseProtos.collectList().block());
  }

  static List<SortParameterAndResult> provideSortParameterAndResult() {
    return List.of(
        new SortParameterAndResult(
            Flux.just(
                RelationshipResponseProto.newBuilder()
                    .setLevelInformation(
                        LevelInformationProto.newBuilder()
                            .setLevel(1)
                            .build()
                    )
                    .build(),
                RelationshipResponseProto.newBuilder()
                    .setLevelInformation(
                        LevelInformationProto.newBuilder()
                            .setLevel(2)
                            .build()
                    )
                    .build(),
                RelationshipResponseProto.newBuilder()
                    .setLevelInformation(
                        LevelInformationProto.newBuilder()
                            .setLevel(3)
                            .build()
                    )
                    .build()
            ),
            SortType.LEVEL,
            Flux.just(
                RelationshipResponseProto.newBuilder()
                    .setLevelInformation(
                        LevelInformationProto.newBuilder()
                            .setLevel(3)
                            .build()
                    )
                    .build(),
                RelationshipResponseProto.newBuilder()
                    .setLevelInformation(
                        LevelInformationProto.newBuilder()
                            .setLevel(2)
                            .build()
                    )
                    .build(),
                RelationshipResponseProto.newBuilder()
                    .setLevelInformation(
                        LevelInformationProto.newBuilder()
                            .setLevel(1)
                            .build()
                    )
                    .build()
            )
        ),
        new SortParameterAndResult(
            Flux.just(
                RelationshipResponseProto.newBuilder()
                    .setNickname("c")
                    .build(),
                RelationshipResponseProto.newBuilder()
                    .setNickname("b")
                    .build(),
                RelationshipResponseProto.newBuilder()
                    .setNickname("a")
                    .build()
            ),
            SortType.NICKNAME,
            Flux.just(
                RelationshipResponseProto.newBuilder()
                    .setNickname("a")
                    .build(),
                RelationshipResponseProto.newBuilder()
                    .setNickname("b")
                    .build(),
                RelationshipResponseProto.newBuilder()
                    .setNickname("c")
                    .build()
            )
        )
    );
  }

  @RequiredArgsConstructor
  private static class SortParameterAndResult {
    private final Flux<RelationshipResponseProto> relationshipResponseProtos;
    private final SortType sortType;
    private final Flux<RelationshipResponseProto> sortedRelationshipResponseProtos;
  }
}
