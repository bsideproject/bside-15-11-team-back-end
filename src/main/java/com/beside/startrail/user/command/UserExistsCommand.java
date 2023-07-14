package com.beside.startrail.user.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.document.UserId;
import com.beside.startrail.user.repository.UserRepository;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import reactor.core.publisher.Mono;

@EqualsAndHashCode
@Getter
public class UserExistsCommand {
  private final UserId userId;
  private final YnType useYn;

  private Mono<User> result;

  public UserExistsCommand(
      UserId userId,
      YnType useYn
  ) {
    this.userId = userId;
    this.useYn = useYn;
  }

  public Mono<User> execute(UserRepository userRepository) {
    result = userRepository.findUserByUserIdAndUseYn(
        userId,
        useYn
    );

    return result;
  }
}
