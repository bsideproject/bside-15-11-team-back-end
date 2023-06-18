package com.beside.mamgwanboo.user.command;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.user.document.User;
import com.beside.mamgwanboo.user.repository.UserRepository;
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
