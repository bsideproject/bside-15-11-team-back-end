package com.beside.startrail.common.protocolbuffer.common;

import com.beside.startrail.common.type.YnType;
import com.beside.startrail.user.model.AllowInformation;
import java.util.Objects;
import protobuf.common.AllowInformationProto;
import protobuf.common.type.YnTypeProto;

public class AllowInformationProtoUtil {
  private AllowInformationProtoUtil() {
  }

  public static AllowInformationProto toAllowInformationProto(AllowInformation allowInformation) {
    if (Objects.isNull(allowInformation)) {
      return AllowInformationProto
          .newBuilder()
          .clear()
          .build();
    }

    AllowInformationProto.Builder builder = AllowInformationProto
        .newBuilder()
        .clear();

    if (Objects.nonNull(allowInformation.getServiceYn())) {
      builder.setServiceYn(
          YnTypeProto.valueOf(allowInformation.getServiceYn().name())
      );
    }
    if (Objects.nonNull(allowInformation.getPrivateInformationYn())) {
      builder.setPrivateInformationYn(
          YnTypeProto.valueOf(allowInformation.getPrivateInformationYn().name())
      );
    }
    if (Objects.nonNull(allowInformation.getEventMarketingYn())) {
      builder.setEventMarketingYn(
          YnTypeProto.valueOf(allowInformation.getEventMarketingYn().name())
      );
    }

    return builder.build();
  }

  public static AllowInformation toAllowInformation(AllowInformationProto allowInformationProto) {
    if (Objects.isNull(allowInformationProto) || !allowInformationProto.isInitialized()) {
      return null;
    }

    return AllowInformation.builder()
        .serviceYn(YnType.valueOf(allowInformationProto.getServiceYn().name()))
        .privateInformationYn(
            YnType.valueOf(allowInformationProto.getPrivateInformationYn().name())
        )
        .eventMarketingYn(YnType.valueOf(allowInformationProto.getEventMarketingYn().name()))
        .build();
  }
}
