package com.beside.startrail.friend.handler;

import com.google.protobuf.Message;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;
import protobuf.friend.FriendPostProto;
import protobuf.friend.FriendPutProto;

@Component
public class FriendRequestValidator {
  public void createValidate(FriendPostProto friendPostProto) {
    if (friendPostProto.getNicknamesCount() == 0
        || StringUtils.isNotBlank(friendPostProto.getRelationship())) {
      onValidateErrors(friendPostProto);
    }
  }

  public void updateValidate(FriendPutProto friendPutProto) {
    if (StringUtils.isNotBlank(friendPutProto.getNickname())
        || StringUtils.isNotBlank(friendPutProto.getRelationship())) {
      onValidateErrors(friendPutProto);
    }
  }

  private void onValidateErrors(Message message) {
    throw new IllegalArgumentException(String.format("필수 파라미터 미존재. message: %s", message));
  }
}
