package com.beside.startrail.user.command;

import com.beside.startrail.user.document.User;
import com.beside.startrail.user.repository.UserRepository;
import reactor.core.publisher.Mono;

public class UserSaveOneCommand {
  private final User user;
  private Mono<User> result;

  public UserSaveOneCommand(User user) {
    this.user = user;
  }

  public Mono<User> execute(UserRepository userRepository) {
    result = userRepository.save(user);

    return result;
  }
}
