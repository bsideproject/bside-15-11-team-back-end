package com.beside.mamgwanboo.sign.kakao.type;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.beside.mamgwanboo.user.type.AgeRangeType;

import lombok.Getter;

@Getter
public enum KakaoAgeRangeType {
	UNDER_TEEN("1~9", AgeRangeType.UNDER_TEEN),
	LOW_TEENS("10~14", AgeRangeType.TEENS),
	HIGH_TEENS("15~20", AgeRangeType.TEENS),
	TWENTIES("20~29", AgeRangeType.TWENTIES),
	THIRTIES("30~39", AgeRangeType.THIRTIES),
	FORTIES("40~49", AgeRangeType.FORTIES),
	FIFTIES("50~59", AgeRangeType.FIFTIES),
	SIXTIES("60~69", AgeRangeType.OVER_FIFTY),
	SEVENTIES("70~79", AgeRangeType.OVER_FIFTY),
	EIGHTIES("80~89", AgeRangeType.OVER_FIFTY),
	OVER_EIGHTY("90~", AgeRangeType.OVER_FIFTY);
	private final String value;
	private final AgeRangeType ageRangeType;
	private static final Map<String, KakaoAgeRangeType> valueMap = Arrays.stream(KakaoAgeRangeType.values())
		.collect(Collectors.toMap(KakaoAgeRangeType::getValue, Function.identity()));

	public static KakaoAgeRangeType fromValue(String value) {
		if (!valueMap.containsKey(value)) {
			return null;
		}

		return valueMap.get(value);
	}

	KakaoAgeRangeType(String value, AgeRangeType ageRangeType) {
		this.value = value;
		this.ageRangeType = ageRangeType;
	}
}
