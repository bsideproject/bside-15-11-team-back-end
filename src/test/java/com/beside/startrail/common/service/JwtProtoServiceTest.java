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
import protobuf.sign.JwtPayloadProto;

class JwtProtoServiceTest {
  private static final String baseKey = "baseKeybaseKeybaseKeybaseKeybaseKey";
  private JwtProtoService jwtProtoService;

  @BeforeEach
  void init() {
    jwtProtoService = new JwtProtoService(baseKey, new ObjectMapper());
  }

  @Test
  void makeJwt() {
    // given
    String sequence = "sequence";

    JwtPayloadProto jwtPayloadProto = JwtPayloadProto.newBuilder()
        .setSequence(sequence)
        .build();

    // when
    String jwt = jwtProtoService.makeJwtProto(jwtPayloadProto)
        .block();

    // then
    assertThat(jwt).isEqualTo(
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.ewogICJzZXF1ZW5jZSI6ICJzZXF1ZW5jZSIKfQ.x0gx0U6N6XxAqdd2wyUT2h8MzyRAwhJGhFahHXisOwg");
  }

  @ParameterizedTest
  @ValueSource(strings = {"asdf", "asdf.asdf"})
  void getPayloadWhenWrongFormatJwt(String jwt) {
    // when
    // then
    assertThrows(MalformedJwtException.class, () -> jwtProtoService.getPayload(jwt).block());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
          "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzZXF1ZW5jZSI6InNlcXVlbmNlIiwiZGV2ZWlsIjoiZGV2aWwifQ.o2HUVhOaaoEP4sihte8JxHgLh_dF0FNdEd_I6YFEOok",
          "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJkZXZlaWwiOiJkZXZpbCJ9.CWgHf3CiBkwsomhzYnubvkDRl6BbbmtPCq0ZseWNBVs"
      }
  )
  void getPayloadWhenWrongPayloadJwt(String jwt) {
    // when
    // then
    assertThrows(IllegalArgumentException.class,
        () -> jwtProtoService.getPayload(jwt).block());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
          "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzZXF1ZW5jZSI6InNlcXVlbmNlIn0.pEciHGkwDIZ2h7Q9-_eN95hTODKDAe1dX_aNTfVX8KI",
          "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzZXF1ZW5jZSI6InNlcXVlbmNlIn0.YlfNHAbVQ6sCLQVUWuCQ_2FtNhFt4N-PPuGxGyczMNo"
      }
  )
  void getPayloadWhenWrongSignedMamgwanbooJwt(String jwt) {
    // when
    // then
    assertThrows(SignatureException.class, () -> jwtProtoService.getPayload(jwt).block());
  }

  @Test
  void getPayload() {
    // given
    String jwt =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.ewogICJzZXF1ZW5jZSI6ICJzZXF1ZW5jZSIKfQ.x0gx0U6N6XxAqdd2wyUT2h8MzyRAwhJGhFahHXisOwg";

    // when
    JwtPayloadProto jwtPayloadProto = jwtProtoService.getPayload(jwt)
        .block();

    // then
    assertThat(jwtPayloadProto).isEqualTo(JwtPayloadProto.newBuilder()
        .setSequence("sequence")
        .build());
  }
}