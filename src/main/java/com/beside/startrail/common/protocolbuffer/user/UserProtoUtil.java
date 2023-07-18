package com.beside.startrail.common.protocolbuffer.user;

import com.beside.startrail.common.protocolbuffer.common.AllowInformationProtoUtil;
import com.beside.startrail.common.protocolbuffer.common.UserInformationProtoUtil;
import com.beside.startrail.user.document.User;
import java.util.Objects;
import protobuf.user.UserPatchRequestProto;
import protobuf.user.UserResponseProto;

public class UserProtoUtil {
  private UserProtoUtil() {
  }

  public static UserResponseProto toUserResponseProto(User user) {
    if (Objects.isNull(user)) {
      return UserResponseProto
          .newBuilder()
          .clear()
          .build();
    }

    return UserResponseProto
        .newBuilder()
        .clear()
        .setUserInformation(
            UserInformationProtoUtil.toUserInformationProto(user.getUserInformation())
        )
        .setAllowInformation(
            AllowInformationProtoUtil.toAllowInformationProto(user.getAllowInformation())
        )
        .build();
  }

  public static User toUser(
      UserPatchRequestProto userPatchRequestProto
  ) {
    if (Objects.isNull(userPatchRequestProto) || !userPatchRequestProto.isInitialized()) {
      return null;
    }

    return User.builder()
        .userInformation(
            UserInformationProtoUtil.toUserInformation(userPatchRequestProto.getUserInformation())
        )
        .allowInformation(
            AllowInformationProtoUtil.toAllowInformation(
                userPatchRequestProto.getAllowInformation()
            )
        )
        .build();
  }
}
