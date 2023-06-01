package com.beside.mamgwanboo.sign.kakao.model;

import java.time.MonthDay;
import java.time.Year;

import com.beside.mamgwanboo.sign.kakao.deserializer.KakaoAgeRangeTypeDeserializer;
import com.beside.mamgwanboo.sign.kakao.deserializer.KakaoBirtyDayTypeDeserializer;
import com.beside.mamgwanboo.sign.kakao.deserializer.KakaoGenderTypeDeserializer;
import com.beside.mamgwanboo.sign.kakao.type.KakaoAgeRangeType;
import com.beside.mamgwanboo.sign.kakao.type.KakaoBirthdayType;
import com.beside.mamgwanboo.sign.kakao.type.KakaoGenderType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;

@Getter
public class KakaoAccount {
	@JsonProperty("profile")
	private KakaoProfile kakaoProfile;
	@JsonProperty("age_range")
	@JsonDeserialize(using = KakaoAgeRangeTypeDeserializer.class)
	private KakaoAgeRangeType kakaoAgeRangeType;
	@JsonProperty("birthyear")
	@JsonFormat(pattern = "yyyy")
	private Year birthYear;
	@JsonProperty("birthday")
	@JsonFormat(pattern = "MMdd")
	private MonthDay birthDay;
	@JsonProperty("birthday_type")
	@JsonDeserialize(using = KakaoBirtyDayTypeDeserializer.class)
	private KakaoBirthdayType kakaoBirthdayType;
	@JsonProperty("gender")
	@JsonDeserialize(using = KakaoGenderTypeDeserializer.class)
	private KakaoGenderType kakaoGenderType;
}
