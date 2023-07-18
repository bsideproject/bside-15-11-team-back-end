package com.beside.startrail.sign.kakao.type;

import com.beside.startrail.user.type.SexType;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum KakaoGenderType {
  MALE("male", SexType.MALE),
  FEMALE("female", SexType.FEMALE);

  private static final Map<String, KakaoGenderType> valueMap =
      Arrays.stream(KakaoGenderType.values())
          .collect(Collectors.toMap(KakaoGenderType::getValue, Function.identity()));
  private final String value;
  private final SexType sexType;

  KakaoGenderType(String value, SexType sexType) {
    this.value = value;
    this.sexType = sexType;
  }

  public static KakaoGenderType fromValue(String value) {
    if (!valueMap.containsKey(value)) {
      return null;
    }

    return valueMap.get(value);
  }

}
