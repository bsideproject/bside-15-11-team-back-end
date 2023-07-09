package com.beside.startrail.sign.apple.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class AppleAccessTokenResponse {
	private Data data;

	@Getter
	public class Data {
		@JsonProperty("id_token")
		private String idToken;
	}
}
