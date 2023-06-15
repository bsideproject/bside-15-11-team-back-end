package com.beside.mamgwanboo.common.errorattribute;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
  @Override
  public Map<String, Object> getErrorAttributes(
      ServerRequest serverRequest,
      ErrorAttributeOptions options
  ) {
    Map<String, Object> attributes = new HashMap<>();

    Throwable throwable = getError(serverRequest);

    if (throwable instanceof IllegalArgumentException) {
      attributes.put("status", HttpStatus.BAD_REQUEST);
      attributes.put("message", "잘못된 요청입니다.");
      return attributes;
    }

    attributes.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
    attributes.put("message", "알 수 없는 오류입니다.");
    return attributes;
  }
}
