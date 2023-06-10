package com.beside.mamgwanboo.sign.common.command;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.user.document.User;
import com.beside.mamgwanboo.user.model.UserInformation;
import protobuf.common.type.OauthServiceType;
import reactor.core.publisher.Mono;

public interface SignCommand {
  boolean isTargetService(OauthServiceType oauthServiceType);

  Mono<String> getAccessToken(String code);

  Mono<UserInformation> getUserInformation(
      OauthServiceType oauthServiceType,
      String accessToken
  );

  Mono<User> getUser(
      OauthServiceType oauthServiceType, String serviceUserId,
      YnType useYn
  );

  Mono<User> signUp(User user);
}
