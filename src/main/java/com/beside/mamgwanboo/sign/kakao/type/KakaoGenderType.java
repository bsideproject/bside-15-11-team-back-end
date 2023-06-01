package com.beside.mamgwanboo.sign.kakao.type;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.beside.mamgwanboo.user.type.SexType;

import lombok.Getter;

@Getter
public enum KakaoGenderType {
	MALE("male", SexType.MALE),
	FEMALE("female", SexType.FEMALE);

	private final String value;
	private final SexType sexType;
	private static final Map<String, KakaoGenderType> valueMap = Arrays.stream(KakaoGenderType.values())
		.collect(Collectors.toMap(KakaoGenderType::getValue, Function.identity()));

	public static KakaoGenderType fromValue(String value) {
		if (!valueMap.containsKey(value)) {
			return null;
		}

		return valueMap.get(value);
	}

	KakaoGenderType(String value, SexType sexType) {
		this.value = value;
		this.sexType = sexType;
	}

}
