package com.beside.mamgwanboo.common.jwt.service;

import protobuf.sign.MamgwanbooJwtPayload;
import reactor.core.publisher.Mono;

public interface JwtService {
	public Mono<String> makeMamgwanbooJwt(
		MamgwanbooJwtPayload mamgwanbooJwtPayload
	);
}
