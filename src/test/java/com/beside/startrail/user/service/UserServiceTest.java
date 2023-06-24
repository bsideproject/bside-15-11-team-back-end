package com.beside.startrail.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.command.UserCreateCommand;
import com.beside.startrail.user.command.UserExistsCommand;
import com.beside.startrail.user.command.UserFindCommand;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.type.OauthServiceType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

class UserServiceTest {
  @ParameterizedTest
  @MethodSource("provideAllOauthServiceTypesAndYnTypes")
  void getUser(OauthServiceTypeYnType oauthServiceTypeYnType) {
    // given
    OauthServiceType oauthServiceType = oauthServiceTypeYnType.oauthServiceType;
    String serviceUserId = "serviceUserId";
    YnType ynType = oauthServiceTypeYnType.ynType;

    // when
    UserFindCommand actual = UserService.getUser(oauthServiceType, serviceUserId, ynType);

    // then
    UserFindCommand expect = new UserFindCommand(oauthServiceType, serviceUserId, ynType);

    assertThat(actual).isEqualTo(expect);
  }

  @Test
  void createUser() {
    // given
    User user = User.builder()
        .build();

    // when
    UserCreateCommand actual = UserService.createUser(user);

    // then
    UserCreateCommand expect = new UserCreateCommand(user);

    assertThat(actual).isEqualTo(expect);
  }

  @ParameterizedTest
  @EnumSource(
      value = YnType.class,
      names = {},
      mode = EnumSource.Mode.EXCLUDE
  )
  void existsUser(YnType ynType) {
    // given
    String uuid = UUID.randomUUID().toString();

    // when
    UserExistsCommand actual = UserService.existsUser(uuid, ynType);

    // then
    UserExistsCommand expect = new UserExistsCommand(uuid, ynType);

    assertThat(actual).isEqualTo(expect);
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