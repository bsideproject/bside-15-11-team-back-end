package com.beside.mamgwanboo.sign.kakao.model;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class KakaoOauthRequest {
	@Builder.Default
	@JsonProperty("grant_type")
	private final String grantType = "authorization_code";
	@JsonProperty("client_id")
	private String clientId;
	@JsonProperty("redirect_uri")
	private String redirectUri;
	@JsonProperty("client_secret")
	private String clientSecret;
	private String code;

	public MultiValueMap<String, String> toFromData() {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", grantType);
		formData.add("client_id", clientId);
		formData.add("redirect_uri", redirectUri);
		formData.add("client_secret", clientSecret);
		formData.add("code", code);

		return formData;
	}
}
