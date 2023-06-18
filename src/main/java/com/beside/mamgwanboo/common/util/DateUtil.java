package com.beside.mamgwanboo.common.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import protobuf.common.Date;

public class DateUtil {
  private DateUtil() {
  }

  public static Date toDate(LocalDateTime localDateTime) {
    return Date.newBuilder()
        .setYear(localDateTime.getYear())
        .setMonth(localDateTime.getMonthValue())
        .setDay(localDateTime.getDayOfMonth())
        .build();
  }

  public static LocalDateTime toLocalDateTime(Date date) {
    LocalTime minLocalTime = LocalTime.MIN;
    return LocalDateTime.of(
        date.getYear(),
        date.getMonth(),
        date.getDay(),
        minLocalTime.getHour(),
        minLocalTime.getMinute()
    );
  }
}
