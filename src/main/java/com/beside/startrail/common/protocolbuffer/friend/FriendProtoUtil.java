package com.beside.startrail.common.protocolbuffer.friend;

import com.beside.startrail.friend.document.Friend;
import protobuf.friend.FriendPostProto;
import protobuf.friend.FriendPutProto;
import protobuf.friend.FriendResponseProto;

import java.util.List;
import java.util.stream.Collectors;

public class FriendProtoUtil {

    public static FriendResponseProto toFriendResponseProto(Friend friend){
        return FriendResponseProto.newBuilder()
                .setSequence(friend.getSequence())
                .setNickname(friend.getNickname())
                .setRelationship(friend.getRelationship())
                .setBirth(friend.getBirth())
                .setMemo(friend.getMemo())
                .setLevelInformation(friend.getLevelInformation())
                .build();
    }

    public static List<Friend> postProtoToDocuments(String userSequence,
                                                    FriendPostProto friendPostProto){
        return friendPostProto.getNicknamesList().stream()
                .map(nickname -> Friend.builder()
                        .userSequence(userSequence)
                        .nickname(nickname)
                        .relationship(friendPostProto.getRelationship())
                        .birth(friendPostProto.getBirth())
                        .memo(friendPostProto.getMemo())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public static Friend updateDtoToDocument(Friend friend,
                                             FriendPutProto friendPutProto){
        return Friend.builder()
                .sequence(friend.getSequence())
                .userSequence(friend.getUserSequence())
                .nickname(friendPutProto.getNickname())
                .relationship(friendPutProto.getRelationship())
                .birth(friendPutProto.getBirth())
                .memo(friendPutProto.getMemo())
                .levelInformation(friend.getLevelInformation())
                .build();
    }
}
