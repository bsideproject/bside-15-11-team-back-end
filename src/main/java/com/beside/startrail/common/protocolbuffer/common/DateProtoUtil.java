package com.beside.startrail.common.protocolbuffer.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import protobuf.common.DateProto;

public class DateProtoUtil {
  private DateProtoUtil() {
  }

  public static DateProto toDate(LocalDateTime localDateTime) {
    if (Objects.isNull(localDateTime)) {
      return DateProto
          .newBuilder()
          .clear()
          .build();
    }

    return toDate(localDateTime.toLocalDate());
  }

  public static DateProto toDate(LocalDate localDate) {
    if (Objects.isNull(localDate)) {
      return DateProto
          .newBuilder()
          .clear()
          .build();
    }

    return DateProto
        .newBuilder()
        .setYear(localDate.getYear())
        .setMonth(localDate.getMonthValue())
        .setDay(localDate.getDayOfMonth())
        .build();
  }

  public static LocalDateTime toLocalDateTime(DateProto dateProto) {
    if (Objects.isNull(dateProto) || isInvalidDate(dateProto)) {
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

  public static LocalDate toLocalDate(DateProto dateProto) {
    if (Objects.isNull(dateProto) || isInvalidDate(dateProto)) {
      return null;
    }

    return LocalDate.of(
        dateProto.getYear(),
        dateProto.getMonth(),
        dateProto.getDay()
    );
  }

  private static boolean isInvalidDate(DateProto dateProto) {
    return dateProto.getYear() == 0
        || dateProto.getMonth() == 0
        || dateProto.getDay() == 0;
  }
}
