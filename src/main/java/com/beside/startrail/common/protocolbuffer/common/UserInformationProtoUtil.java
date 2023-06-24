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
      return null;
    }

    return UserInformationProto.newBuilder()
        .setProfileNickname(userInformation.getProfileNickname())
        .setProfileImageLink(userInformation.getProfileImageLink())
        .setSexType(
            SexTypeProto.valueOf(userInformation.getSexType().name())
        )
        .setAgeRangeType(
            AgeRangeTypeProto.valueOf(userInformation.getAgeRangeType().name())
        )
        .setBirth(DateProtoUtil.toDate(userInformation.getBirth()))
        .build();
  }

  public static UserInformation toUserInformation(UserInformationProto userInformationProto) {
    if (Objects.isNull(userInformationProto)) {
      return null;
    }

    return UserInformation.builder()
        .profileNickname(userInformationProto.getProfileNickname())
        .profileImageLink(userInformationProto.getProfileImageLink())
        .sexType(
            SexType.valueOf(userInformationProto.getSexType().name())
        )
        .ageRangeType(
            AgeRangeType.valueOf(userInformationProto.getAgeRangeType().name())
        )
        .birth(DateProtoUtil.toLocalDateTime(userInformationProto.getBirth()))
        .build();
  }
}
