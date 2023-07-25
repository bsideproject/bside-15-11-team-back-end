package com.beside.startrail.common.protocolbuffer.friend;

import com.beside.startrail.common.protocolbuffer.common.DateProtoUtil;
import com.beside.startrail.common.type.YnType;
import com.beside.startrail.friend.document.Birth;
import com.beside.startrail.friend.document.Friend;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import protobuf.common.BirthProto;
import protobuf.common.LevelInformationProto;
import protobuf.common.type.YnTypeProto;
import protobuf.friend.FriendPostProto;
import protobuf.friend.FriendPutProto;
import protobuf.friend.FriendResponseProto;

public class FriendProtoUtil {

  public static FriendResponseProto toFriendResponseProto(Friend friend) {
    if (Objects.isNull(friend)) {
      return FriendResponseProto
          .newBuilder()
          .clear()
          .build();
    }

    FriendResponseProto.Builder builder = FriendResponseProto
        .newBuilder()
        .clear();

    if (Objects.nonNull(friend.getSequence())) {
      builder.setSequence(friend.getSequence());
    }
    if (Objects.nonNull(friend.getNickname())) {
      builder.setNickname(friend.getNickname());
    }
    if (Objects.nonNull(friend.getRelationship())) {
      builder.setRelationship(friend.getRelationship());
    }
    if (Objects.nonNull(friend.getBirth())) {
      builder.setBirth(toBirthProto(friend.getBirth()));
    }
    if (Objects.nonNull(friend.getNickname())) {
      builder.setNickname(friend.getNickname());
    }
    if (Objects.nonNull(friend.getNickname())) {
      builder.setNickname(friend.getNickname());
    }

    return FriendResponseProto.newBuilder()
        .setSequence(friend.getSequence())
        .setNickname(friend.getNickname())
        .setRelationship(friend.getRelationship())
        .setBirth(toBirthProto(friend.getBirth()))
        .setMemo(friend.getMemo())
        .setLevelInformation(LevelInformationProto.newBuilder().build())
        .build();
  }

  public static List<Friend> toFriends(
      String userSequence,
      FriendPostProto friendPostProto
  ) {
    if (Objects.isNull(friendPostProto) || !friendPostProto.isInitialized()) {
      return null;
    }

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

  public static Friend toFriends(
      Friend friend,
      FriendPutProto friendPutProto
  ) {
    if (Objects.isNull(friendPutProto) || !friendPutProto.isInitialized()) {
      return null;
    }

    return Friend.builder()
        .sequence(friend.getSequence())
        .userSequence(friend.getUserSequence())
        .nickname(friendPutProto.getNickname())
        .relationship(friendPutProto.getRelationship())
        .birth(toBirth(friendPutProto.getBirth()))
        .memo(friendPutProto.getMemo())
        .build();
  }

  public static Birth toBirth(BirthProto birthProto) {
    if (Objects.isNull(birthProto) || !birthProto.isInitialized()) {
      return null;
    }

    return Birth.builder()
        .isLunar(YnType.valueOf(birthProto.getIsLunar().name()))
        .date(DateProtoUtil.toLocalDate(birthProto.getDate()))
        .build();
  }

  private static BirthProto toBirthProto(Birth birth) {
    if (Objects.isNull(birth)) {
      return BirthProto.newBuilder()
          .clear()
          .build();
    }

    BirthProto.Builder builder = BirthProto
        .newBuilder()
        .clear();

    if (Objects.nonNull(birth.getIsLunar())) {
      builder.setIsLunar(YnTypeProto.valueOf(birth.getIsLunar().name()));
    }
    if (Objects.nonNull(birth.getDate())) {
      builder.setDate(DateProtoUtil.toDate(birth.getDate()));
    }

    return builder.build();
  }
}
