package com.beside.mamgwanboo.user.service;

import com.beside.mamgwanboo.common.type.YnType;
import com.beside.mamgwanboo.user.command.UserCreateCommand;
import com.beside.mamgwanboo.user.command.UserExistsCommand;
import com.beside.mamgwanboo.user.command.UserFindCommand;
import com.beside.mamgwanboo.user.document.User;
import protobuf.common.type.OauthServiceType;

public class UserService {
  public static UserFindCommand getUser(
      OauthServiceType oauthServiceType,
      String serviceUserId,
      YnType useYn
  ) {
    return new UserFindCommand(oauthServiceType, serviceUserId, useYn);
  }

  public static UserCreateCommand createUser(User user) {
    return new UserCreateCommand(user);
  }

  public static UserExistsCommand existsUser(String sequence, YnType useYn) {
    return new UserExistsCommand(sequence, useYn);
  }
}
