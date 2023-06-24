package com.beside.startrail.user.service;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.command.UserCreateCommand;
import com.beside.startrail.user.command.UserExistsCommand;
import com.beside.startrail.user.command.UserFindCommand;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.type.OauthServiceType;

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
