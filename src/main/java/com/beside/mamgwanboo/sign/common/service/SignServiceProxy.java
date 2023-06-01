package com.beside.mamgwanboo.sign.common.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import protobuf.common.type.OauthServiceType;
import protobuf.sign.SignRequest;
import reactor.core.publisher.Mono;

@Component
public class SignServiceProxy {
	private final List<AbstractSignService> abstractSignServices;

	public SignServiceProxy(List<AbstractSignService> abstractSignServices) {
		this.abstractSignServices = abstractSignServices;
	}

	public Mono<String> sign(SignRequest signRequest) {
		return Optional.ofNullable(getTargetService(signRequest.getOauthServiceType()))
			.map(abstractSignService -> abstractSignService.sign(signRequest.getOauthServiceType(), signRequest.getCode()))
			.orElse(Mono.empty());
	}

	private AbstractSignService getTargetService(OauthServiceType oauthServiceType) {
		return abstractSignServices.stream()
			.filter(abstractSignService -> abstractSignService.isTargetService(oauthServiceType))
			.findFirst()
			.orElse(null);
	}
}
