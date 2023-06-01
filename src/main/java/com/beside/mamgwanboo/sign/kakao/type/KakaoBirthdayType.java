package com.beside.mamgwanboo.sign.kakao.type;

import com.beside.mamgwanboo.common.type.YnType;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum KakaoBirthdayType {
  SOLAR("SOLAR", YnType.N),
  LUNAR("LUNAR", YnType.Y);

  private static final Map<String, KakaoBirthdayType> valueMap =
      Arrays.stream(KakaoBirthdayType.values())
          .collect(Collectors.toMap(KakaoBirthdayType::getValue, Function.identity()));
  private final String value;
  private final YnType isLunar;

  KakaoBirthdayType(String value, YnType isLunar) {
    this.value = value;
    this.isLunar = isLunar;
  }

  public static KakaoBirthdayType fromValue(String value) {
    if (!valueMap.containsKey(value)) {
      return null;
    }

    return valueMap.get(value);
  }
}
