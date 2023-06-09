package com.beside.mamgwanboo.common.util;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import reactor.core.publisher.Mono;

public final class ProtocolBufferUtil {
  private ProtocolBufferUtil() {
  }

  public static <T extends Message> Mono<T> parse(String body, T.Builder messageBuilder) {
    try {
      JsonFormat.parser().merge(body, messageBuilder);

      return Mono.just((T) messageBuilder.build());
    } catch (InvalidProtocolBufferException invalidProtocolBufferException) {
      return Mono.error(new IllegalArgumentException(
          String.format("message 파싱에 실패했습니다. body: %s, type: %s", body,
              messageBuilder.getDescriptorForType()), invalidProtocolBufferException));
    }
  }

  public static String print(Message message) {
    try {
      return JsonFormat.printer()
          .includingDefaultValueFields()
          .print(message);
    } catch (InvalidProtocolBufferException invalidProtocolBufferException) {
      throw new IllegalArgumentException(
          String.format("message json 변환에 실패했습니다. message: %s", message.toString()),
          invalidProtocolBufferException);
    }
  }
}
