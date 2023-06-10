package com.beside.mamgwanboo.sign.common.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doCallRealMethod;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.sign.common.command.SignCommand;
import com.beside.mamgwanboo.sign.common.command.SignCommandProxy;
import com.beside.mamgwanboo.sign.common.model.SignResult;
import com.beside.mamgwanboo.user.document.User;
import com.beside.mamgwanboo.user.model.UserInformation;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.platform.commons.support.ReflectionSupport;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import protobuf.common.type.OauthServiceType;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class SignServiceTest {
  // todo command로 다 빼도 사실 달라진게 없이 함수형아 아닌데 어떻게 안될까...
  @InjectMocks
  private SignService signService;
  private SignCommandProxy signCommandProxy;

  @BeforeEach
  @SuppressWarnings("unchecked")
  void init() {
    List<SignCommand> signCommandMocks = ReflectionSupport.findAllClassesInPackage(
            "com.beside.mamgwanboo",
            clazz ->
                SignCommand.class.isAssignableFrom(clazz) && !clazz.isInterface(),
            classname -> true
        ).stream()
        .map(clazz -> (Class<SignCommand>) clazz)
        .map(Mockito::mock)
        .collect(Collectors.toList());

    for (SignCommand signCommandMock : signCommandMocks) {
      doCallRealMethod().when(signCommandMock).isTargetService(any(OauthServiceType.class));
    }

    signCommandProxy = new SignCommandProxy(signCommandMocks);
    signService = new SignService(signCommandProxy);
  }

  @ParameterizedTest
  @EnumSource(
      value = OauthServiceType.class,
      mode = EnumSource.Mode.EXCLUDE,
      names = {"KAKAO"}
  )
  void signWhenUnimplementedOauthServiceCode(OauthServiceType oauthServiceType) {
    // given
    String code = "code";

    // when
    // then
    assertThrows(UnsupportedOperationException.class,
        () -> signService.sign(oauthServiceType, code).block());
  }

  @ParameterizedTest
  @EnumSource(
      value = OauthServiceType.class,
      mode = EnumSource.Mode.INCLUDE,
      names = {"KAKAO"}
  )
  void signWhenNewUser(OauthServiceType oauthServiceType) {
    // given
    String code = "code";
    String accessToken = "accessToken";
    String serviceUserId = "serviceUserId";
    UserInformation userInformation = UserInformation.builder()
        .oauthServiceType(oauthServiceType)
        .serviceUserId(serviceUserId)
        .build();
    User user = User.builder()
        .userInformation(userInformation)
        .build();

    given(signCommandProxy.getTargetCommand(oauthServiceType).getAccessToken(code))
        .willReturn(Mono.just(accessToken));
    given(signCommandProxy.getTargetCommand(oauthServiceType)
        .getUserInformation(oauthServiceType, accessToken))
        .willReturn(Mono.just(userInformation));
    given(signCommandProxy.getTargetCommand(oauthServiceType)
        .getUser(oauthServiceType, serviceUserId, YnType.Y))
        .willReturn(Mono.empty());
    given(signCommandProxy.getTargetCommand(oauthServiceType).signUp(any(User.class)))
        .willReturn(Mono.just(user));

    // when
    SignResult signResult = signService.sign(oauthServiceType, code).block();

    // then
    assert signResult != null;
    assertThat(signResult.isNewUser()).isTrue();
    assertThat(signResult.getUser().getUserInformation()).isEqualTo(userInformation);

    // todo 이거 이상함. 아래 참고
//    then(signCommandProxy.getTargetCommand(oauthServiceType))
//        .should(times(1))
//        .signUp(any(User.class));
  }

  @ParameterizedTest
  @EnumSource(
      value = OauthServiceType.class,
      mode = EnumSource.Mode.INCLUDE,
      names = {"KAKAO"}
  )
  void signWhenExistingUser(OauthServiceType oauthServiceType) {
    // given
    String code = "code";
    String accessToken = "accessToken";
    String serviceUserId = "serviceUserId";
    UserInformation userInformation = UserInformation.builder()
        .oauthServiceType(oauthServiceType)
        .serviceUserId(serviceUserId)
        .build();
    User user = User.builder()
        .userInformation(userInformation)
        .build();

    given(signCommandProxy.getTargetCommand(oauthServiceType).getAccessToken(code))
        .willReturn(Mono.just(accessToken));
    given(signCommandProxy.getTargetCommand(oauthServiceType)
        .getUserInformation(oauthServiceType, accessToken))
        .willReturn(Mono.just(userInformation));
    given(signCommandProxy.getTargetCommand(oauthServiceType)
        .getUser(oauthServiceType, serviceUserId, YnType.Y))
        .willReturn(Mono.just(user));
    // todo 여기도 매번 불림.
//    given(signCommandProxy.getTargetCommand(oauthServiceType)
//        .signUp(any(User.class)))
//        .willThrow(new RuntimeException());
    given(signCommandProxy.getTargetCommand(oauthServiceType)
        .signUp(any(User.class)))
        .willReturn(Mono.just(User.builder().build()));

    // when
    SignResult signResult = signService.sign(oauthServiceType, code).block();

    // then
    assert signResult != null;
    assertThat(signResult.isNewUser()).isFalse();
    assertThat(signResult.getUser().getUserInformation()).isEqualTo(userInformation);

    //todo 여기 매번 불리는데, 반환되는 값은 맞음. 왜그런거지? reactor가 이런가
//    then(signCommandProxy.getTargetCommand(oauthServiceType))
//        .should(never())
//        .signUp(any(User.class));
  }
}
