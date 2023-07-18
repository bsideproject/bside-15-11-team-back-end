package com.beside.startrail.common.protocolbuffer.common;

import com.beside.startrail.user.model.UserInformation;
import com.beside.startrail.user.type.AgeRangeType;
import com.beside.startrail.user.type.SexType;
import java.util.Objects;
import protobuf.common.UserInformationProto;
import protobuf.common.type.AgeRangeTypeProto;
import protobuf.common.type.SexTypeProto;

public class UserInformationProtoUtil {
  private UserInformationProtoUtil() {
  }

  public static UserInformationProto toUserInformationProto(UserInformation userInformation) {
    if (Objects.isNull(userInformation)) {
      return UserInformationProto
          .newBuilder()
          .clear()
          .build();
    }

    UserInformationProto.Builder builder = UserInformationProto
        .newBuilder()
        .clear();

    if (Objects.nonNull(userInformation.getProfileNickname())) {
      builder.setProfileNickname(userInformation.getProfileNickname());
    }
    if (Objects.nonNull(userInformation.getProfileImageUrl())) {
      builder.setProfileImageLink(userInformation.getProfileImageUrl());
    }
    if (Objects.nonNull(userInformation.getSexType())) {
      builder.setSexType(SexTypeProto.valueOf(userInformation.getSexType().name()));
    }
    if (Objects.nonNull(userInformation.getAgeRangeType())) {
      builder.setAgeRangeType(AgeRangeTypeProto.valueOf(userInformation.getAgeRangeType().name()));
    }
    if (Objects.nonNull(userInformation.getBirth())) {
      builder.setBirth(DateProtoUtil.toDate(userInformation.getBirth()));
    }

    return builder
        .build();
  }

  public static UserInformation toUserInformation(UserInformationProto userInformationProto) {
    if (Objects.isNull(userInformationProto) || !userInformationProto.isInitialized()) {
      return null;
    }

    return UserInformation.builder()
        .profileNickname(userInformationProto.getProfileNickname())
        .profileImageUrl(userInformationProto.getProfileImageLink())
        .sexType(SexType.valueOf(userInformationProto.getSexType().name()))
        .ageRangeType(AgeRangeType.valueOf(userInformationProto.getAgeRangeType().name()))
        .birth(DateProtoUtil.toLocalDateTime(userInformationProto.getBirth()))
        .build();
  }
}
