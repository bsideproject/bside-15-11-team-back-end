package com.beside.startrail.user.command;

import com.beside.startrail.common.command.Command;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.repository.UserRepository;
import reactor.core.publisher.Mono;

public class UserSaveOneCommand implements Command<Mono<User>, UserRepository> {
  private final User user;

  private Mono<User> result;

  public UserSaveOneCommand(User user) {
    this.user = user;
  }

  @Override
  public Mono<User> execute(UserRepository userRepository) {
    result = userRepository.save(user);

    return result;
  }
}
