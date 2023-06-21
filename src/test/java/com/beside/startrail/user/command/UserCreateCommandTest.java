package com.beside.startrail.user.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.beside.startrail.user.document.User;
import com.beside.startrail.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class UserCreateCommandTest {
  @Mock
  UserRepository userRepository;

  @Test
  void execute() {
    // given
    User user = User.builder().build();
    Mono<User> expect = Mono.just(user);
    UserCreateCommand userCreateCommand = new UserCreateCommand(user);

    given(userRepository.save(user)).willReturn(expect);

    // when
    Mono<User> actual = userCreateCommand.execute(userRepository);

    // then
    assertThat(actual.block())
        .isEqualTo(expect.block())
        .isEqualTo(userCreateCommand.getResult().block());
  }
}