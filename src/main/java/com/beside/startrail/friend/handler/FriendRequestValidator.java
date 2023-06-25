package com.beside.startrail.friend.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import protobuf.friend.FriendPostProto;
import protobuf.friend.FriendPutProto;

@Slf4j
@Component
public class FriendRequestValidator {
    public void createValidate(FriendPostProto friendPostProto){
        if(friendPostProto.getNicknamesCount() == 0
                || !friendPostProto.hasRelationship())
            onValidateErrors();
    }

    public void updateValidate(FriendPutProto friendPutProto){
        if(!friendPutProto.hasNickname() || !friendPutProto.hasRelationship())
            onValidateErrors();
    }

    private void onValidateErrors(){
        log.error("필수 파라미터 미존재.");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "....");
    }
}
