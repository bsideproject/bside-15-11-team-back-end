package com.beside.mamgwanboo.sign.common.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.beside.mamgwanboo.common.jwt.service.JwtService;
import com.beside.mamgwanboo.common.util.ProtocolBufferUtil;
import com.beside.mamgwanboo.sign.common.service.SignServiceProxy;
import protobuf.sign.MamgwanbooJwtPayload;
import protobuf.sign.SignRequest;
import reactor.core.publisher.Mono;

@Component
public class SignHandler {
	private final SignServiceProxy signServiceProxy;
	private final JwtService jwtService;

	public SignHandler(SignServiceProxy signServiceProxy, JwtService jwtService) {
		this.signServiceProxy = signServiceProxy;
		this.jwtService = jwtService;
	}

	public Mono<ServerResponse> sign(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(String.class)
			.flatMap(body -> ProtocolBufferUtil.<SignRequest>parse(body, SignRequest.newBuilder()))
			.flatMap(signServiceProxy::sign)
			.map(sequence -> MamgwanbooJwtPayload.newBuilder().setSequence(String.valueOf(sequence)).build())
			.map(jwtService::makeMamgwanbooJwt)
			.flatMap(jwt -> ServerResponse
				.ok()
				.bodyValue(jwt));
	}
}

