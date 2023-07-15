package com.beside.startrail.friend.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.friend.document.Friend;
import com.beside.startrail.friend.repository.FriendRepository;
import reactor.core.publisher.Flux;

public class FriendFindByUserSequenceCommand {
  private final String userSequence;
  private Flux<Friend> result;

  public FriendFindByUserSequenceCommand(String userSequence) {
    this.userSequence = userSequence;
  }

  public Flux<Friend> execute(FriendRepository friendRepository) {
    result = friendRepository.findByUserSequenceAndUseYn(
        userSequence,
        YnType.Y
    );

    return result;
  }
}
