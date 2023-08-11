package com.beside.startrail.relationship.service;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.command.RelationshipFindAllByUserSequenceAndNicknameKeywordAndUseYnCommand;
import com.beside.startrail.relationship.command.RelationshipFindAllByUserSequenceCommand;
import com.beside.startrail.relationship.command.RelationshipFindOneByUserSequenceAndSequenceAndUseYnCommand;
import com.beside.startrail.relationship.command.RelationshipSaveOneCommand;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.type.SortType;
import java.util.Comparator;
import java.util.Optional;
import protobuf.common.LevelInformationProto;
import protobuf.relationship.RelationshipResponseProto;
import reactor.core.publisher.Flux;

public class RelationshipService {
  public static Flux<RelationshipResponseProto> sort(
      Flux<RelationshipResponseProto> relationshipResponseProtos,
      SortType sortType
  ) {
    switch (sortType) {
      case LEVEL -> {
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

  public static RelationshipSaveOneCommand create(Relationship relationship) {
    return new RelationshipSaveOneCommand(relationship);
  }

  public static RelationshipFindAllByUserSequenceCommand getByUserSequenceAndUseYn(
      String userSequence,
      YnType useYn
  ) {
    return new RelationshipFindAllByUserSequenceCommand(userSequence, useYn);
  }
}
