package com.beside.mamgwanboo.sign.kakao.type;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.beside.mamgwanboo.common.type.YnType;

import lombok.Getter;

@Getter
public enum KakaoBirthdayType {
	SOLAR("SOLAR", YnType.N),
	LUNAR("LUNAR", YnType.Y);

	private final String value;
	private final YnType isLunar;
	private static final Map<String, KakaoBirthdayType> valueMap = Arrays.stream(KakaoBirthdayType.values())
		.collect(Collectors.toMap(KakaoBirthdayType::getValue, Function.identity()));

	public static KakaoBirthdayType fromValue(String value) {
		if (!valueMap.containsKey(value)) {
			return null;
		}

		return valueMap.get(value);
	}

	KakaoBirthdayType(String value, YnType isLunar) {
		this.value = value;
		this.isLunar = isLunar;
	}
}
