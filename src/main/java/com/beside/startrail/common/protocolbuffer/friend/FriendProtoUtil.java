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
import protobuf.friend.FriendPostRequestProto;
import protobuf.friend.FriendPutRequestProto;
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
      FriendPostRequestProto friendPostRequestProto
  ) {
    if (Objects.isNull(friendPostRequestProto) || !friendPostRequestProto.isInitialized()) {
      return null;
    }

    return friendPostRequestProto.getNicknamesList().stream()
        .map(nickname -> Friend.builder()
            .userSequence(userSequence)
            .nickname(nickname)
            .relationship(friendPostRequestProto.getRelationship())
            .birth(toBirth(friendPostRequestProto.getBirth()))
            .memo(friendPostRequestProto.getMemo())
            .build()
        )
        .collect(Collectors.toList());
  }

  public static Friend toFriends(
      Friend friend,
      FriendPutRequestProto friendPutRequestProto
  ) {
    if (Objects.isNull(friendPutRequestProto) || !friendPutRequestProto.isInitialized()) {
      return null;
    }

    return Friend.builder()
        .sequence(friend.getSequence())
        .userSequence(friend.getUserSequence())
        .nickname(friendPutRequestProto.getNickname())
        .relationship(friendPutRequestProto.getRelationship())
        .birth(toBirth(friendPutRequestProto.getBirth()))
        .memo(friendPutRequestProto.getMemo())
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
