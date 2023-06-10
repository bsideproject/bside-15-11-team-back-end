package com.beside.mamgwanboo.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.protobuf.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import protobuf.sign.SignRequest;

class ProtocolBufferUtilTest {
  @ParameterizedTest
  @ValueSource(strings = {
      "asdf",
      "{\n" +
          "  \"a\": \"a\"\n" +
          "}"
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
    String body =
        "{\n" +
            "  \"oauthServiceType\": \"KAKAO\",\n" +
            "  \"code\": \"\"\n" +
            "}";
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
            "{\n" +
                "  \"oauthServiceType\": \"KAKAO\",\n" +
                "  \"code\": \"\"\n" +
                "}"
        );
  }
}