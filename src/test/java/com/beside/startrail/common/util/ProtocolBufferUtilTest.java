package com.beside.startrail.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.google.protobuf.Message;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import protobuf.sign.JwtPayloadProto;

class ProtocolBufferUtilTest {
  @ParameterizedTest
  @ValueSource(strings = {
      "asdf",
      """
          {
            "a": "a"
          }"""
  })
  void parseWhenWrongBody(String body) {
    // given
    Message.Builder builder = JwtPayloadProto.newBuilder();

    // when
    // then
    assertThrows(IllegalArgumentException.class,
        () -> ProtocolBufferUtil.parse(body, builder).block());
  }

  @Test
  void parse() {
    // given
    String body = """
        {"sequence": ""}
        """;
    Message.Builder builder = JwtPayloadProto.newBuilder();
    Message expected = builder.build();

    // when
    Message actual = ProtocolBufferUtil.parse(body, builder).block();

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void print() {
    // given
    Message message = JwtPayloadProto.newBuilder().build();

    // when
    String json = ProtocolBufferUtil.print(message);

    // then
    assertThat(json)
        .isEqualTo(
            """
                {
                  "sequence": ""
                }"""
        );
  }

  @Test
  void from() {
    // given
    MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
    UUID uuid = UUID.randomUUID();
    multiValueMap.add("sequence", uuid.toString());
    JwtPayloadProto.Builder builder = JwtPayloadProto.newBuilder();

    // when
    JwtPayloadProto jwtPayloadProto =
        ProtocolBufferUtil.<JwtPayloadProto>from(multiValueMap, builder).block();

    // then
    assertThat(jwtPayloadProto.getSequence())
        .isEqualTo(uuid.toString());
  }
}