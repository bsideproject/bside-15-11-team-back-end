package com.beside.mamgwanboo.sign.type;

import lombok.Getter;

@Getter
public enum KakaoGenderType {
	MALE("male"),
	FEMALE("female");

	private String value;

	KakaoGenderType(String value) {
		this.value = value;
	}
}
