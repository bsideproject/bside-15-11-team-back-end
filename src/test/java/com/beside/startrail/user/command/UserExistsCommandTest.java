package com.beside.startrail.user.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class UserExistsCommandTest {
  @Mock
  UserRepository userRepository;

  @ParameterizedTest
  @MethodSource("provideAllYnTypeBooleans")
  void execute(YnTypeBoolean ynTypeBoolean) {
    // given
    String uuid = UUID.randomUUID().toString();
    YnType ynType = ynTypeBoolean.ynType;
    Mono<Boolean> expect = Mono.justOrEmpty(ynTypeBoolean.bool);
    UserExistsCommand userExistsCommand = new UserExistsCommand(uuid, ynType);

    given(userRepository.existsBySequenceAndUseYn(uuid, ynType)).willReturn(expect);

    // when
    Mono<Boolean> actual = userExistsCommand.execute(userRepository);

    // then
    assertThat(actual.block())
        .isEqualTo(expect.block())
        .isEqualTo(userExistsCommand.getResult().block());
  }

  private static List<YnTypeBoolean> provideAllYnTypeBooleans() {
    List<YnTypeBoolean> ynTypeBooleans = new ArrayList<>();

    List<Boolean> booleans = new ArrayList<>();
    booleans.add(null);
    booleans.add(Boolean.FALSE);
    booleans.add(Boolean.TRUE);

    for (YnType ynType : YnType.values()) {
      for (Boolean bool : booleans) {
        ynTypeBooleans.add(new YnTypeBoolean(ynType, bool));
      }
    }

    return ynTypeBooleans;
  }

  @AllArgsConstructor
  static class YnTypeBoolean {
    private YnType ynType;
    private Boolean bool;
  }
}