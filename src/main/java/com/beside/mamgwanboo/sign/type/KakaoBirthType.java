package com.beside.mamgwanboo.sign.type;

import lombok.Getter;
import lombok.val;

@Getter
public enum KakaoBirthType {
	SOLAR("SOLAR"),
	LUNAR("LUNAR");

	private String value;

	KakaoBirthType(String value) {
		this.value = value;
	}
}
