package com.beside.startrail.common.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import protobuf.sign.JwtPayload;

class JwtServiceTest {
  private static final String baseKey = "baseKeybaseKeybaseKeybaseKeybaseKey";
  private JwtService jwtService;

  @BeforeEach
  void init() {
    jwtService = new JwtService(baseKey, new ObjectMapper());
  }

  @Test
  void makeJwt() {
    // given
    String sequence = "sequence";

    JwtPayload jwtPayload = JwtPayload.newBuilder()
        .setSequence(sequence)
        .build();

    // when
    String mamwagnbooJwt = jwtService.makeJwt(jwtPayload)
        .block();

    // then
    assertThat(mamwagnbooJwt).isEqualTo(
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.ewogICJzZXF1ZW5jZSI6ICJzZXF1ZW5jZSIKfQ.x0gx0U6N6XxAqdd2wyUT2h8MzyRAwhJGhFahHXisOwg");
  }

  @ParameterizedTest
  @ValueSource(strings = {"asdf", "asdf.asdf"})
  void getPayloadWhenWrongFormatJwt(String mamgwanbooJwt) {
    // when
    // then
    assertThrows(MalformedJwtException.class, () -> jwtService.getPayload(mamgwanbooJwt).block());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
          "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzZXF1ZW5jZSI6InNlcXVlbmNlIiwiZGV2ZWlsIjoiZGV2aWwifQ.o2HUVhOaaoEP4sihte8JxHgLh_dF0FNdEd_I6YFEOok",
          "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJkZXZlaWwiOiJkZXZpbCJ9.CWgHf3CiBkwsomhzYnubvkDRl6BbbmtPCq0ZseWNBVs"
      }
  )
  void getPayloadWhenWrongPayloadJwt(String mamgwanbooJwt) {
    // when
    // then
    assertThrows(IllegalArgumentException.class,
        () -> jwtService.getPayload(mamgwanbooJwt).block());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
          "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzZXF1ZW5jZSI6InNlcXVlbmNlIn0.pEciHGkwDIZ2h7Q9-_eN95hTODKDAe1dX_aNTfVX8KI",
          "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzZXF1ZW5jZSI6InNlcXVlbmNlIn0.YlfNHAbVQ6sCLQVUWuCQ_2FtNhFt4N-PPuGxGyczMNo"
      }
  )
  void getPayloadWhenWrongSignedMamgwanbooJwt(String mamgwanbooJwt) {
    // when
    // then
    assertThrows(SignatureException.class, () -> jwtService.getPayload(mamgwanbooJwt).block());
  }

  @Test
  void getPayload() {
    // given
    String mamwanbooJwt =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.ewogICJzZXF1ZW5jZSI6ICJzZXF1ZW5jZSIKfQ.x0gx0U6N6XxAqdd2wyUT2h8MzyRAwhJGhFahHXisOwg";

    // when
    JwtPayload jwtPayload = jwtService.getPayload(mamwanbooJwt)
        .block();

    // then
    assertThat(jwtPayload).isEqualTo(JwtPayload.newBuilder()
        .setSequence("sequence")
        .build());
  }
}