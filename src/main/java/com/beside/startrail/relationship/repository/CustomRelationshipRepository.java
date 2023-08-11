package com.beside.startrail.relationship.repository;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.relationship.document.Relationship;
import reactor.core.publisher.Flux;


public interface CustomRelationshipRepository {
  Flux<Relationship> findAllRelationshipByUserSequenceAndNicknameKeywordAndUseYn(
      String userSequence,
      String nicknameKeyword,
      YnType useYn
  );
}
