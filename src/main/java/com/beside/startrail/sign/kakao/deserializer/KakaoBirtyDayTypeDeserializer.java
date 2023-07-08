package com.beside.startrail.sign.kakao.deserializer;

import com.beside.startrail.sign.kakao.type.KakaoBirthdayType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class KakaoBirtyDayTypeDeserializer extends JsonDeserializer<KakaoBirthdayType> {
  @Override
  public KakaoBirthdayType deserialize(
      JsonParser jsonParser,
      DeserializationContext deserializationContext
  ) throws IOException {
    return KakaoBirthdayType.fromValue(jsonParser.getValueAsString());
  }
}
