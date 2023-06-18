package com.beside.mamgwanboo.user.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.user.document.User;
import com.beside.mamgwanboo.user.document.UserId;
import com.beside.mamgwanboo.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import protobuf.common.type.OauthServiceType;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class UserFindCommandTest {
  @Mock
  UserRepository userRepository;

  @ParameterizedTest
  @MethodSource("provideAllOauthServiceTypesAndYnTypes")
  void execute(OauthServiceTypeYnType oauthServiceTypeYnType) {
    // given
    OauthServiceType oauthServiceType = oauthServiceTypeYnType.oauthServiceType;
    String serviceUserId = "serviceUserId";
    YnType ynType = oauthServiceTypeYnType.ynType;
    User user = User.builder()
        .userId(
            UserId.builder()
                .oauthServiceType(oauthServiceType)
                .serviceUserId(serviceUserId)
                .build()
        )
        .useYn(ynType)
        .build();
    Mono<User> expect = Mono.just(user);
    UserFindCommand userFindCommand = new UserFindCommand(oauthServiceType, serviceUserId, ynType);

    given(
        userRepository.findUserByUserId_OauthServiceType_AndUserId_ServiceUserIdAndUseYn(
            oauthServiceType,
            serviceUserId,
            ynType)
    )
        .willReturn(expect);

    // when
    Mono<User> actual = userFindCommand.execute(userRepository);

    // then
    assertThat(actual.block())
        .isEqualTo(expect.block())
        .isEqualTo(userFindCommand.getResult().block());
  }


  private static List<OauthServiceTypeYnType> provideAllOauthServiceTypesAndYnTypes() {
    List<OauthServiceTypeYnType> oauthServiceTypeYnTypes = new ArrayList<>();

    for (OauthServiceType oauthServiceType : OauthServiceType.values()) {
      for (YnType ynType : YnType.values()) {
        oauthServiceTypeYnTypes.add(new OauthServiceTypeYnType(oauthServiceType, ynType));
      }
    }
    return oauthServiceTypeYnTypes;
  }

  @AllArgsConstructor
  static class OauthServiceTypeYnType {
    private OauthServiceType oauthServiceType;
    private YnType ynType;
  }
}