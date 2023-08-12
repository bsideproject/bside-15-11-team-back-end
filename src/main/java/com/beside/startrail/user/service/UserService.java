package com.beside.startrail.user.service;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.command.UserExistsByUserIdAndUseYnCommand;
import com.beside.startrail.user.command.UserFindOneBySequenceAndUseYnCommand;
import com.beside.startrail.user.command.UserFindOneByUserIdAndUseYnCommand;
import com.beside.startrail.user.command.UserSaveOneCommand;
import com.beside.startrail.user.document.User;
import com.beside.startrail.user.document.UserId;

public class UserService {
  public static UserExistsByUserIdAndUseYnCommand existsByUserId(
      UserId userId,
      YnType useYn
  ) {
    return new UserExistsByUserIdAndUseYnCommand(
        userId,
        useYn
    );
  }

  public static UserFindOneByUserIdAndUseYnCommand getByUserId(
      UserId userId,
      YnType useYn
  ) {
    return new UserFindOneByUserIdAndUseYnCommand(
        userId,
        useYn
    );
  }

  public static UserFindOneBySequenceAndUseYnCommand getBySequence(
      String sequence,
      YnType useYn
  ) {
    return new UserFindOneBySequenceAndUseYnCommand(
        sequence,
        useYn
    );
  }

  public static UserSaveOneCommand create(User user) {
    return new UserSaveOneCommand(user);
  }
}
