package com.beside.mamgwanboo.sign.common.service;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.sign.common.model.SignResult;
import com.beside.mamgwanboo.user.document.User;
import com.beside.mamgwanboo.user.model.UserInformation;
import java.util.Objects;
import protobuf.common.type.OauthServiceType;
import reactor.core.publisher.Mono;

public abstract class AbstractSignService {
  public Mono<SignResult> sign(
      OauthServiceType oauthServiceType,
      String authenticationCode
  ) {
    return getAccessToken(authenticationCode)
        .flatMap(accessToken -> getUserInformation(oauthServiceType, accessToken))
        .flatMap(userInformation -> getUser(
            userInformation.getOauthServiceType(),
            userInformation.getServiceUserId(),
            YnType.Y)
            .filter(Objects::nonNull)
            .map(user -> SignResult.builder()
                .user(user)
                .isNewUser(false)
                .build())
            .switchIfEmpty(signUp(makeUser(userInformation))
                .map(user -> SignResult.builder()
                    .isNewUser(true)
                    .build())));
  }

  private User makeUser(UserInformation userInformation) {
    return User.builder()
        .userInformation(userInformation)
        .useYn(YnType.Y)
        .build();
  }

  public abstract boolean isTargetService(OauthServiceType oauthServiceType);

  public abstract Mono<String> getAccessToken(String code);

  public abstract Mono<UserInformation> getUserInformation(OauthServiceType oauthServiceType,
                                                           String accessToken);

  public abstract Mono<User> getUser(OauthServiceType oauthServiceType, String serviceUserId,
                                     YnType useYn);

  public abstract Mono<User> signUp(User user);
}
