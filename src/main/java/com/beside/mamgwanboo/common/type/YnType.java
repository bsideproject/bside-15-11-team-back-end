package com.beside.mamgwanboo.common.type;

import lombok.Getter;

@Getter
public enum YnType {
	Y("Y"),
	N("N");

	private final String value;

	YnType(String value) {
		this.value = value;
	}
}
