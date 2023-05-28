package com.beside.mamgwanboo.sign.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import protobuf.sign.SignRequest;
import protobuf.type.OauthServiceType;
import reactor.core.publisher.Mono;

import lombok.Setter;

@Setter
public class SignRequestDto {
	private OauthServiceType oauthServiceType;
	private String authenticationCode;

	@JsonCreator
	public static OauthServiceType from(String oauthServiceType) {
		return OauthServiceType.valueOf(oauthServiceType);
	}

	public static Mono<SignRequest> to(SignRequestDto signRequestDto) {
		return Mono.just(SignRequest.newBuilder()
			.setOauthServiceType(signRequestDto.oauthServiceType)
			.setAuthenticationCode(signRequestDto.authenticationCode)
			.build());
	}
}

