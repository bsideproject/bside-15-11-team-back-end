package com.beside.startrail.sign.apple.model;

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
public class AppleAccessTokenRequest {
	@Builder.Default
	@JsonProperty("grant_type")
	private final String grantType = "authorization_code";
	@JsonProperty("client_id")
	private String clientId;
	@JsonProperty("client_secret")
	private String clientSecret;
	@JsonProperty("redirect_uri")
	private String redirectUri;
	private String code;

	public MultiValueMap<String, String> toMultiValueMap() {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("grant_type", grantType);
		formData.add("client_id", clientId);
		formData.add("client_secret", clientSecret);
		formData.add("redirect_uri", redirectUri);
		formData.add("code", code);

		return formData;
	}
}
