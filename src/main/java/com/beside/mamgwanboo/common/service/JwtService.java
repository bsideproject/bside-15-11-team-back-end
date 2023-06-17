package com.beside.mamgwanboo.common.service;

import com.beside.mamgwanboo.common.util.ProtocolBufferUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import protobuf.sign.MamgwanbooJwtPayload;
import reactor.core.publisher.Mono;

@Service
public class JwtService {
  private static final String HEADER_ALGORITHM_KEY = "alg";
  private static final String HEADER_ALGORITHM_HS256 = "HS256";
  private final String baseKey;
  private final ObjectMapper objectMapper;

  public JwtService(@Value("${jwt.baseKey}") String baseKey, ObjectMapper objectMapper) {
    this.baseKey = baseKey;
    this.objectMapper = objectMapper;
  }

  public Mono<String> makeJwt(MamgwanbooJwtPayload mamgwanbooJwtPayload) {
    return Mono.just(
            ProtocolBufferUtil.print(mamgwanbooJwtPayload)
        )
        .map(payload ->
            Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setHeaderParam(HEADER_ALGORITHM_KEY, HEADER_ALGORITHM_HS256)
                .setPayload(payload)
                .signWith(Keys.hmacShaKeyFor(baseKey.getBytes(StandardCharsets.UTF_8)))
                .compact());
  }

  public Mono<MamgwanbooJwtPayload> getPayload(String mamgwanbooJwt) {
    return Mono.just(
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(baseKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(mamgwanbooJwt)
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
        .flatMap(jsonString ->
            ProtocolBufferUtil.parse(jsonString, MamgwanbooJwtPayload.newBuilder())
        );
  }

  public Boolean isSigned(String jwt) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(Keys.hmacShaKeyFor(baseKey.getBytes(StandardCharsets.UTF_8)))
          .build()
          .parseClaimsJws(jwt);
    } catch (RuntimeException runtimeException) {
      return false;
    }

    return true;
  }
}
