package com.beside.mamgwanboo.common.jwt.service;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import protobuf.sign.MamgwanbooJwtPayload;

@Service
public class JwtServiceImpl implements JwtService {
  private static final String HEADER_ALGORITHM_KEY = "alg";
  private static final String HEADER_ALGORITHM_HS256 = "HS256";
  private final String baseKey;

  public JwtServiceImpl(@Value("${jwt.baseKey}") String baseKey) {
    this.baseKey = baseKey;
  }

  @Override
  public String makeMamgwanbooJwt(
      MamgwanbooJwtPayload mamgwanbooJwtPayload
  ) {
    return Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setHeaderParam(HEADER_ALGORITHM_KEY, HEADER_ALGORITHM_HS256)
        .setPayload(mamgwanbooJwtPayload.getSequence())
        .signWith(Keys.hmacShaKeyFor(baseKey.getBytes(StandardCharsets.UTF_8)))
        .compact();
  }
}
