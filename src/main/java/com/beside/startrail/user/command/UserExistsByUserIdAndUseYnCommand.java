package com.beside.startrail.user.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.document.UserId;
import com.beside.startrail.user.repository.UserRepository;
import reactor.core.publisher.Mono;

public class UserExistsByUserIdAndUseYnCommand {
  private final UserId userId;
  private final YnType useYn;

  private Mono<Boolean> result;

  public UserExistsByUserIdAndUseYnCommand(UserId userId, YnType useYn) {
    this.userId = userId;
    this.useYn = useYn;
  }

  public Mono<Boolean> execute(UserRepository userRepository) {
    result = userRepository.existsByUserIdAndUseYn(userId, useYn);

    return result;
  }
}
