package com.beside.mamgwanboo.sign.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.beside.mamgwanboo.common.jwt.service.JwtService;
import com.beside.mamgwanboo.sign.model.SignRequestDto;
import com.beside.mamgwanboo.sign.service.AbstractSignService;
import protobuf.sign.MamgwanbooJwtPayload;
import reactor.core.publisher.Mono;

@Component
public class SignHandler {
	private final AbstractSignService abstractSignService;
	private final JwtService jwtService;

	public SignHandler(AbstractSignService abstractSignService, JwtService jwtService) {
		this.abstractSignService = abstractSignService;
		this.jwtService = jwtService;
	}

	public Mono<ServerResponse> sign(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(SignRequestDto.class)
			.flatMap(SignRequestDto::to)
			.flatMap(signRequest -> abstractSignService.sign(signRequest.getOauthServiceType(), signRequest.getAuthenticationCode()))
			.map(sequence -> MamgwanbooJwtPayload.newBuilder().setSequence(String.valueOf(sequence)).build())
			.flatMap(jwtService::makeMamgwanbooJwt)
			.flatMap(jwt -> ServerResponse.ok().bodyValue(jwt));
	}
}

