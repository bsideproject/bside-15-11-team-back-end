package com.beside.startrail.common.service;

import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import protobuf.sign.JwtPayloadProto;
import reactor.core.publisher.Mono;

@Service
public class JwtProtoService {
  private static final String HEADER_ALGORITHM_KEY = "alg";
  private static final String HEADER_ALGORITHM_HS256 = "HS256";
  private final String baseKey;
  private final ObjectMapper objectMapper;

  public JwtProtoService(@Value("${jwt.baseKey}") String baseKey, ObjectMapper objectMapper) {
    this.baseKey = baseKey;
    this.objectMapper = objectMapper;
  }

  public Mono<String> makeJwtProto(JwtPayloadProto jwtPayloadProto) {
    return Mono.just(
            ProtocolBufferUtil.print(jwtPayloadProto)
        )
        .map(payload ->
            Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setHeaderParam(HEADER_ALGORITHM_KEY, HEADER_ALGORITHM_HS256)
                .setPayload(payload)
                .signWith(Keys.hmacShaKeyFor(baseKey.getBytes(StandardCharsets.UTF_8)))
                .compact());
  }

  public Mono<JwtPayloadProto> getPayload(String jwt) {
    return Mono.just(
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(baseKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(jwt)
                .getBody()
        )
        .<String>handle((claims, sink) -> {
          try {
            sink.next(objectMapper.writeValueAsString(claims));
          } catch (JsonProcessingException jsonProcessingException) {
            sink.error(new IllegalArgumentException(
                String.format("jwt payload의 json 변환에 실패했습니다. jwt: %s", claims),
                jsonProcessingException
            ));
          }
        })
        .flatMap(json ->
            ProtocolBufferUtil.parse(json, JwtPayloadProto.newBuilder())
        );
  }
}
