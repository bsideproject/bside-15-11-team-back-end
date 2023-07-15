package com.beside.startrail.user.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.repository.UserRepository;
import reactor.core.publisher.Mono;

public class UserFindBySequenceCommand {
  private final String sequence;
  private Mono<User> result;

  public UserFindBySequenceCommand(String sequence) {
    this.sequence = sequence;
  }

  public Mono<User> execute(UserRepository userRepository) {
    result = userRepository.findBySequenceAndUseYn(
        sequence,
        YnType.Y
    );

    return result;
  }
}
