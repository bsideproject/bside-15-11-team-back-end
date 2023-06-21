package com.beside.startrail.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.protobuf.Message;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import protobuf.sign.SignRequest;

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
    Message.Builder builder = SignRequest.newBuilder();

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
    Message.Builder builder = SignRequest.newBuilder();
    Message expected = builder.build();

    // when
    Message actual = ProtocolBufferUtil.parse(body, builder).block();

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void print() {
    // given
    Message message = SignRequest.newBuilder().build();

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
    SignRequest.Builder builder = SignRequest.newBuilder();

    // when
    SignRequest signRequest = ProtocolBufferUtil.<SignRequest>from(multiValueMap, builder).block();

    // then
    assertThat(signRequest.getSequence())
        .isEqualTo(uuid.toString());
  }
}