package com.beside.mamgwanboo.sign.kakao.deserializer;

import com.beside.mamgwanboo.sign.kakao.type.KakaoGenderType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class KakaoGenderTypeDeserializer extends JsonDeserializer<KakaoGenderType> {
  @Override
  public KakaoGenderType deserialize(JsonParser jsonParser,
                                     DeserializationContext deserializationContext)
      throws IOException {
    return KakaoGenderType.fromValue(jsonParser.getValueAsString());
  }
}
