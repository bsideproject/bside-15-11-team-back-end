package com.beside.startrail.friend.command;

import com.beside.startrail.friend.document.Friend;
import com.beside.startrail.friend.repository.FriendRepository;
import reactor.core.publisher.Mono;

public class FriendSaveCommand {
  private final Friend friend;
  private Mono<Friend> result;

  public FriendSaveCommand(Friend friend) {
    this.friend = friend;
  }

  public Mono<Friend> execute(FriendRepository friendRepository) {
    result = friendRepository.save(friend);

    return result;
  }
}
