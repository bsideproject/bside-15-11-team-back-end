package com.beside.startrail.relationship.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.document.Relationship;
import com.beside.startrail.relationship.repository.CustomRelationshipRepository;
import reactor.core.publisher.Flux;

public class RelationshipFindAllByUserSequenceAndNicknameKeywordAndUseYnCommand {
  private final String userSequence;
  private final String nicknameKeyword;
  private final YnType useYn;

  private Flux<Relationship> result;

  public RelationshipFindAllByUserSequenceAndNicknameKeywordAndUseYnCommand(
      String userSequence,
      String nicknameKeyword,
      YnType useYn
  ) {
    this.userSequence = userSequence;
    this.nicknameKeyword = nicknameKeyword;
    this.useYn = useYn;
  }

  public Flux<Relationship> execute(CustomRelationshipRepository customRelationshipRepository) {
    result = customRelationshipRepository.findRelationshipsByCriteria(
        userSequence,
        nicknameKeyword,
        useYn
    );

    return result;
  }
}
