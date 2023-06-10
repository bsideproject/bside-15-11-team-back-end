package com.beside.mamgwanboo.sign.common.service;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.sign.common.command.SignCommandProxy;
import com.beside.mamgwanboo.sign.common.model.SignResult;
import com.beside.mamgwanboo.user.document.User;
import com.beside.mamgwanboo.user.model.UserInformation;
import org.springframework.stereotype.Service;
import protobuf.common.type.OauthServiceType;
import reactor.core.publisher.Mono;

@Service
public class SignService {
  private final SignCommandProxy signCommandProxy;

  public SignService(SignCommandProxy signCommandProxy) {
    this.signCommandProxy = signCommandProxy;
  }

  public Mono<SignResult> sign(
      OauthServiceType oauthServiceType,
      String code
  ) {
    return Mono.justOrEmpty(signCommandProxy.getTargetCommand(oauthServiceType))
        .flatMap(signCommand -> signCommand.getAccessToken(code)
            .flatMap(accessToken ->
                signCommand.getUserInformation(oauthServiceType, accessToken))
            .flatMap(userInformation ->
                signCommand.getUser(
                        userInformation.getOauthServiceType(),
                        userInformation.getServiceUserId(),
                        YnType.Y
                    )
                    .map(user ->
                        SignResult.builder()
                            .user(user)
                            .isNewUser(false)
                            .build()
                    )
                    .switchIfEmpty(signCommand.signUp(makeUser(userInformation))
                        .map(user ->
                            SignResult.builder()
                                .user(user)
                                .isNewUser(true)
                                .build()
                        )
                    )
            )
        );
  }

  private User makeUser(UserInformation userInformation) {
    return User.builder()
        .userInformation(userInformation)
        .useYn(YnType.Y)
        .build();
  }
}
