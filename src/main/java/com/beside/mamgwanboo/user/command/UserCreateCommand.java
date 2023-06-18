package com.beside.mamgwanboo.user.command;

import com.beside.mamgwanboo.user.document.User;
import com.beside.mamgwanboo.user.repository.UserRepository;
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
