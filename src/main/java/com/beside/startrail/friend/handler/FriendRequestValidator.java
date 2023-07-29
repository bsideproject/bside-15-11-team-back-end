package com.beside.startrail.friend.handler;

import com.google.protobuf.Message;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;
import protobuf.friend.FriendPostRequestProto;
import protobuf.friend.FriendPutRequestProto;

@Component
public class FriendRequestValidator {
  public void createValidate(FriendPostRequestProto friendPostProto) {
    if (friendPostProto.getNicknamesCount() == 0
        || StringUtils.isBlank(friendPostProto.getRelationship())) {
      onValidateErrors(friendPostProto);
    }
  }

  public void updateValidate(FriendPutRequestProto friendPutProto) {
    if (StringUtils.isBlank(friendPutProto.getNickname())
        || StringUtils.isBlank(friendPutProto.getRelationship())) {
      onValidateErrors(friendPutProto);
    }
  }

  private void onValidateErrors(Message message) {
    throw new IllegalArgumentException(String.format("필수 파라미터 미존재. message: %s", message));
  }
}
