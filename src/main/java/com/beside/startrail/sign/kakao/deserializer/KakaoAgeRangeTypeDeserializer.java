package com.beside.startrail.sign.kakao.deserializer;

import com.beside.startrail.sign.kakao.type.KakaoAgeRangeType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class KakaoAgeRangeTypeDeserializer extends JsonDeserializer<KakaoAgeRangeType> {
  @Override
  public KakaoAgeRangeType deserialize(
      JsonParser jsonParser,
      DeserializationContext deserializationContext
  )
      throws IOException {
    return KakaoAgeRangeType.fromValue(jsonParser.getValueAsString());
  }
}
