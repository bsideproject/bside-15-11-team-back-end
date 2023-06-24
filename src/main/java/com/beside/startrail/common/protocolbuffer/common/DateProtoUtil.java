package com.beside.startrail.common.protocolbuffer.common;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import protobuf.common.DateProto;

public class DateProtoUtil {
  private DateProtoUtil() {
  }

  public static DateProto toDate(LocalDateTime localDateTime) {
    if (Objects.isNull(localDateTime)) {
      return null;
    }

    return DateProto.newBuilder()
        .setYear(localDateTime.getYear())
        .setMonth(localDateTime.getMonthValue())
        .setDay(localDateTime.getDayOfMonth())
        .build();
  }

  public static LocalDateTime toLocalDateTime(DateProto dateProto) {
    if (Objects.isNull(dateProto)) {
      return null;
    }

    LocalTime minLocalTime = LocalTime.MIN;

    return LocalDateTime.of(
        dateProto.getYear(),
        dateProto.getMonth(),
        dateProto.getDay(),
        minLocalTime.getHour(),
        minLocalTime.getMinute()
    );
  }
}
