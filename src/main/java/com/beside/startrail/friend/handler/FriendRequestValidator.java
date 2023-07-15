package com.beside.startrail.friend.handler;

import com.google.protobuf.Message;
import org.springframework.stereotype.Component;
import protobuf.friend.FriendPostProto;
import protobuf.friend.FriendPutProto;

@Component
public class FriendRequestValidator {
  public void createValidate(FriendPostProto friendPostProto) {
    if (friendPostProto.getNicknamesCount() == 0 || !friendPostProto.hasRelationship()) {
      onValidateErrors(friendPostProto);
    }
  }

  public void updateValidate(FriendPutProto friendPutProto) {
    if (!friendPutProto.hasNickname() || !friendPutProto.hasRelationship()) {
      onValidateErrors(friendPutProto);
    }
  }

  private void onValidateErrors(Message message) {
    throw new IllegalArgumentException(String.format("필수 파라미터 미존재. message: %s", message));
  }
}
