package com.beside.startrail.user.service;

import com.beside.startrail.common.command.Command;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.command.UserExistsByUserIdAndUseYnCommand;
import com.beside.startrail.user.command.UserFindOneBySequenceAndUseYnCommand;
import com.beside.startrail.user.command.UserFindOneByUserIdAndUseYnCommand;
import com.beside.startrail.user.command.UserSaveOneCommand;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.document.UserId;
import com.beside.startrail.user.repository.UserRepository;
import reactor.core.publisher.Mono;

public class UserService {
  public static Command<Mono<Boolean>, UserRepository> existsByUserId(
      UserId userId,
      YnType useYn
  ) {
    return new UserExistsByUserIdAndUseYnCommand(
        userId,
        useYn
    );
  }

  public static Command<Mono<User>, UserRepository> getByUserId(
      UserId userId,
      YnType useYn
  ) {
    return new UserFindOneByUserIdAndUseYnCommand(
        userId,
        useYn
    );
  }

  public static Command<Mono<User>, UserRepository> getBySequence(
      String sequence,
      YnType useYn
  ) {
    return new UserFindOneBySequenceAndUseYnCommand(
        sequence,
        useYn
    );
  }

  public static Command<Mono<User>, UserRepository> create(User user) {
    return new UserSaveOneCommand(user);
  }
}
