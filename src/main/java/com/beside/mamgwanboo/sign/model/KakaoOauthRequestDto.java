package com.beside.mamgwanboo.sign.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class KakaoOauthRequestDto {
	private String grantType = "authorization_code";
	private String clientId;
	private String redirectUri;
	private String code;
	private String clientSecret;
}
