package com.beside.startrail.sign.kakao.model;

import com.beside.startrail.sign.kakao.deserializer.KakaoAgeRangeTypeDeserializer;
import com.beside.startrail.sign.kakao.deserializer.KakaoBirtyDayTypeDeserializer;
import com.beside.startrail.sign.kakao.deserializer.KakaoGenderTypeDeserializer;
import com.beside.startrail.sign.kakao.type.KakaoAgeRangeType;
import com.beside.startrail.sign.kakao.type.KakaoBirthdayType;
import com.beside.startrail.sign.kakao.type.KakaoGenderType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.Year;
import java.util.Objects;
import lombok.Getter;

@Getter
public class KakaoAccount {
  @JsonProperty("profile")
  private KakaoProfile kakaoProfile;
  @JsonProperty("age_range")
  @JsonDeserialize(using = KakaoAgeRangeTypeDeserializer.class)
  private KakaoAgeRangeType kakaoAgeRangeType;
  @JsonProperty("birthyear")
  @JsonFormat(pattern = "yyyy")
  private Year birthYear;
  @JsonProperty("birthday")
  @JsonFormat(pattern = "MMdd")
  private MonthDay birthDay;
  @JsonProperty("birthday_type")
  @JsonDeserialize(using = KakaoBirtyDayTypeDeserializer.class)
  private KakaoBirthdayType kakaoBirthdayType;
  @JsonProperty("gender")
  @JsonDeserialize(using = KakaoGenderTypeDeserializer.class)
  private KakaoGenderType kakaoGenderType;

  public LocalDateTime getBirth() {
    int year = Objects.isNull(birthYear)
        ? kakaoAgeRangeType.getAgeRangeType().getDefaultBirthYear() : birthYear.getValue();
    int month = Objects.isNull(birthDay)
        ? 1 : birthDay.getMonth().getValue();
    int day = Objects.isNull(birthDay)
        ? 1 : birthDay.getDayOfMonth();

    return LocalDate.of(
            year,
            month,
            day
        )
        .atStartOfDay();
  }
}
