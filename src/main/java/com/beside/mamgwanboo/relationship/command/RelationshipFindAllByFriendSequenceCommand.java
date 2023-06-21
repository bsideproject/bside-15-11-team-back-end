package com.beside.mamgwanboo.relationship.command;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.relationship.document.Relationship;
import com.beside.mamgwanboo.relationship.repository.RelationshipRepository;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;

public class RelationshipFindAllByFriendSequenceCommand {
  private final String userSequence;
  private final String friendSequence;
  private final Sort sort;
  private Flux<Relationship> result;

  public RelationshipFindAllByFriendSequenceCommand(
      String userSequence,
      String friendSequence,
      Sort sort
  ) {
    this.userSequence = userSequence;
    this.friendSequence = friendSequence;
    this.sort = sort;
  }

  public Flux<Relationship> execute(RelationshipRepository relationshipRepository) {
    result = relationshipRepository.findAllByUserSequenceAndFriendSequenceAndUseYn(userSequence,
        friendSequence, YnType.Y, sort);

    return result;
  }
}
