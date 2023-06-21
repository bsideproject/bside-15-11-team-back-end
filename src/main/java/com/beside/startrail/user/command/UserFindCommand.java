package com.beside.startrail.user.command;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.repository.UserRepository;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import protobuf.common.type.OauthServiceType;
import reactor.core.publisher.Mono;

@EqualsAndHashCode
@Getter
public class UserFindCommand {
  private final OauthServiceType oauthServiceType;
  private final String serviceUserId;
  private final YnType useYn;
  private Mono<User> result;

  public UserFindCommand(
      OauthServiceType oauthServiceType,
      String serviceUserId,
      YnType useYn
  ) {
    this.oauthServiceType = oauthServiceType;
    this.serviceUserId = serviceUserId;
    this.useYn = useYn;
  }

  public Mono<User> execute(UserRepository userRepository) {
    result = userRepository.findUserByUserId_OauthServiceType_AndUserId_ServiceUserIdAndUseYn(
        oauthServiceType,
        serviceUserId,
        useYn
    );

    return result;
  }
}
