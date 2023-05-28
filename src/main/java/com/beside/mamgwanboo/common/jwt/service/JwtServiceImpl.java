package com.beside.mamgwanboo.common.jwt.service;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import protobuf.sign.MamgwanbooJwtPayload;
import reactor.core.publisher.Mono;

@Service
public class JwtServiceImpl implements JwtService {
	private static final String HEADER_ALGORITHM_KEY = "alg";
	private static final String HEADER_ALGORITHM_HS256 = "HS256";
	private final String baseKey;

	public JwtServiceImpl(@Value("${jwt.baseKey}") String baseKey) {
		this.baseKey = baseKey;
	}

	@Override
	public Mono<String> makeMamgwanbooJwt(
		MamgwanbooJwtPayload mamgwanbooJwtPayload
	) {
		return Mono.just(
			Jwts.builder()
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.setHeaderParam(HEADER_ALGORITHM_KEY, HEADER_ALGORITHM_HS256)
				.setPayload(mamgwanbooJwtPayload.getSequence())
				.signWith(Keys.hmacShaKeyFor(baseKey.getBytes(StandardCharsets.UTF_8)))
				.compact());
	}
}
