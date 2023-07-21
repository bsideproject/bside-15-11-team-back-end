package com.beside.startrail.common.protocolbuffer.user;

import com.beside.startrail.user.document.UserId;
import com.beside.startrail.user.type.OauthServiceType;
import java.util.Objects;
import protobuf.common.UserIdProto;

public class UserIdProtoUtil {
  UserIdProtoUtil() {
  }

  public static UserId toUserId(UserIdProto userIdProto) {
    if (Objects.isNull(userIdProto)) {
      return null;
    }

    return UserId
        .builder()
        .oauthServiceType(
            OauthServiceType.valueOf(userIdProto.getOauthServiceType().name())
        )
        .serviceUserId(userIdProto.getServiceUserId())
        .build();
  }
}
