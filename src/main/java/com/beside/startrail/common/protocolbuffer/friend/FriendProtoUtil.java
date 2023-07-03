package com.beside.startrail.common.protocolbuffer.friend;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.friend.document.Birth;
import com.beside.startrail.friend.document.Friend;
import org.springframework.util.ObjectUtils;
import protobuf.common.BirthProto;
import protobuf.common.DateProto;
import protobuf.common.LevelInformationProto;
import protobuf.common.type.YnTypeProto;
import protobuf.friend.FriendPostProto;
import protobuf.friend.FriendPutProto;
import protobuf.friend.FriendResponseProto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class FriendProtoUtil {

    public static FriendResponseProto toFriendResponseProto(Friend friend){
        return FriendResponseProto.newBuilder()
                .setSequence(friend.getSequence())
                .setNickname(friend.getNickname())
                .setRelationship(friend.getRelationship())
                .setBirth(toBirthProto(friend.getBirth()))
                .setMemo(friend.getMemo())
                .setLevelInformation(LevelInformationProto.newBuilder().build())
                .build();
    }

    public static List<Friend> postProtoToDocuments(String userSequence,
                                                    FriendPostProto friendPostProto){
        return friendPostProto.getNicknamesList().stream()
                .map(nickname -> Friend.builder()
                        .userSequence(userSequence)
                        .nickname(nickname)
                        .relationship(friendPostProto.getRelationship())
                        .birth(toBirth(friendPostProto.getBirth()))
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
                .birth(toBirth(friendPutProto.getBirth()))
                .memo(friendPutProto.getMemo())
                .build();
    }

    public static Birth toBirth(BirthProto birthProto){
        if(ObjectUtils.isEmpty(birthProto) || !birthProto.hasDate())
            return Birth.builder().build();

        return Birth.builder()
                .isLunar(YnType.valueOf(birthProto.getIsLunar().name()))
                .date(
                        LocalDate.of(
                                birthProto.getDate().getYear(),
                                birthProto.getDate().getMonth(),
                                birthProto.getDate().getDay()
                        )
                )
                .build();
    }
    public static BirthProto toBirthProto(Birth birth){
        if(ObjectUtils.isEmpty(birth))
            return BirthProto.newBuilder().build();

        return BirthProto.newBuilder()
                .setIsLunar(YnTypeProto.valueOf(birth.getIsLunar().name()))
                .setDate(
                        DateProto.newBuilder()
                            .setYear(birth.getDate().getYear())
                            .setMonth(birth.getDate().getMonthValue())
                            .setDay(birth.getDate().getDayOfMonth())
                        .build()
                )
                .build();
    }
}
