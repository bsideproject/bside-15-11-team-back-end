package com.beside.startrail.common.protocolbuffer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

public final class ProtocolBufferUtil {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private ProtocolBufferUtil() {
  }

  @SuppressWarnings("unchecked")
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

  public static <T extends Message> String printAll(Collection<T> messages) {
    return "["
        + messages.stream()
        .map(ProtocolBufferUtil::print)
        .collect(Collectors.joining(","))
        + "]";
  }

  @SuppressWarnings("unchecked")
  public static <T extends Message> Mono<T> from(
      MultiValueMap<String, String> multiValueMap,
      T.Builder messageBuilder
  ) {
    String jsonMultiValueMap = null;

    try {
      jsonMultiValueMap = OBJECT_MAPPER.writeValueAsString(multiValueMap);
    } catch (JsonProcessingException jsonProcessingException) {
      throw new IllegalArgumentException(
          String.format("map을 json으로 만드는 데 실패했습니다. map: %s", messageBuilder),
          jsonProcessingException);
    }

    return parse(jsonMultiValueMap, messageBuilder);
  }
}
