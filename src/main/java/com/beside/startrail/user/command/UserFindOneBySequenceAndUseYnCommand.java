package com.beside.startrail.user.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.repository.UserRepository;
import reactor.core.publisher.Mono;

public class UserFindOneBySequenceAndUseYnCommand {
  private final String sequence;
  private final YnType useYn;

  private Mono<User> result;

  public UserFindOneBySequenceAndUseYnCommand(
      String sequence,
      YnType useYn
  ) {
    this.sequence = sequence;
    this.useYn = useYn;
  }

  public Mono<User> execute(UserRepository userRepository) {
    result = userRepository.findBySequenceAndUseYn(
        sequence,
        useYn
    );

    return result;
  }
}
