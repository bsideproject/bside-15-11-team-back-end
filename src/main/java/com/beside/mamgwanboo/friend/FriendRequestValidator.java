package com.beside.mamgwanboo.friend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import protobuf.friend.FriendCreateDto;
import protobuf.friend.FriendUpdateDto;

@Slf4j
@Component
public class FriendRequestValidator {
    public void createValidate(FriendCreateDto friendCreateDto){
        if(friendCreateDto.getNicknamesCount() == 0
                || !friendCreateDto.hasRelationship())
            onValidateErrors();
    }

    public void updateValidate(FriendUpdateDto friendUpdateDto){
        if(!friendUpdateDto.hasNickname() || !friendUpdateDto.hasRelationship())
            onValidateErrors();
    }

    private void onValidateErrors(){
        log.error("필수 파라미터 미존재.");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "....");
    }
}
