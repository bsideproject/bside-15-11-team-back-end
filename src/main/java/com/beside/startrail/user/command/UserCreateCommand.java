package com.beside.startrail.user.command;

import com.beside.startrail.user.document.User;
import com.beside.startrail.user.repository.UserRepository;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import reactor.core.publisher.Mono;

@Getter
@EqualsAndHashCode
public class UserCreateCommand {
  private final User user;
  private Mono<User> result;

  public UserCreateCommand(User user) {
    this.user = user;
  }

  public Mono<User> execute(UserRepository userRepository) {
    result = userRepository.save(user);

    return result;
  }
}
