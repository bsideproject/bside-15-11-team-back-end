package com.beside.mamgwanboo.common.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import protobuf.sign.MamgwanbooJwtPayload;

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

    MamgwanbooJwtPayload mamgwanbooJwtPayload = MamgwanbooJwtPayload.newBuilder()
        .setSequence(sequence)
        .build();

    // when
    String mamwagnbooJwt = jwtService.makeJwt(mamgwanbooJwtPayload)
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
    MamgwanbooJwtPayload mamgwanbooJwtPayload = jwtService.getPayload(mamwanbooJwt)
        .block();

    // then
    assertThat(mamgwanbooJwtPayload).isEqualTo(MamgwanbooJwtPayload.newBuilder()
        .setSequence("sequence")
        .build());
  }

  @Test
  void isSignedWhenSigned() {
    // given
    String mamwanbooJwt =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.ewogICJzZXF1ZW5jZSI6ICJzZXF1ZW5jZSIKfQ.x0gx0U6N6XxAqdd2wyUT2h8MzyRAwhJGhFahHXisOwg";

    // when
    boolean result = jwtService.isSigned(mamwanbooJwt);

    // then
    assertTrue(result);
  }

  @Test
  void isSignedWhenUnsigned() {
    // given
    String mamwanbooJwt =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzZXF1ZW5jZSI6ImFzZGYifQ.7xaBbcddICubDbnYRDhVbWWiyQ3XhwzB2-ry6xFJdws";

    // when
    boolean result = jwtService.isSigned(mamwanbooJwt);

    // then
    assertFalse(result);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
          "asdf",
          ""
      }
  )
  void isSignedWhenInvalidJwt(String jwt) {
    // when
    boolean result = jwtService.isSigned(jwt);

    // then
    assertFalse(result);
  }
}