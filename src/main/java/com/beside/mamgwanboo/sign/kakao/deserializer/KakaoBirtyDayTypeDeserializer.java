package com.beside.mamgwanboo.sign.kakao.deserializer;

import java.io.IOException;

import com.beside.mamgwanboo.sign.kakao.type.KakaoBirthdayType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class KakaoBirtyDayTypeDeserializer extends JsonDeserializer<KakaoBirthdayType> {
	@Override
	public KakaoBirthdayType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		return KakaoBirthdayType.fromValue(jsonParser.getValueAsString());
	}
}
