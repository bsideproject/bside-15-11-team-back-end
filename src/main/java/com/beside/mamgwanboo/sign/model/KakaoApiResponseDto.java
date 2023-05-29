package com.beside.mamgwanboo.sign.model;

import java.time.LocalDate;

import com.beside.mamgwanboo.sign.type.KakaoBirthType;
import com.beside.mamgwanboo.sign.type.KakaoGenderType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class KakaoApiResponseDto {
	private long id;
	@JsonProperty("profile")
	private KakaoProfile kakaoProfile;
	@JsonProperty("age_range")
	private String ageRange;
	@JsonProperty("birthyear")
	private LocalDate birthYear;
	@JsonProperty("birthday")
	private LocalDate birthDay;
	@JsonProperty("birthdaty_type")
	private KakaoBirthType kakaoBirthType;
	@JsonProperty("gender")
	private KakaoGenderType kakaoGenderType;
}
