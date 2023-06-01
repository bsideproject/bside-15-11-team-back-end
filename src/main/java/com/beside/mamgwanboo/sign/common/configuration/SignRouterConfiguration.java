package com.beside.mamgwanboo.sign.common.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.beside.mamgwanboo.sign.common.handler.SignHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;

@Configuration
public class SignRouterConfiguration {
  // todo sign 패키지 리팩토링 필요 - 함수형 / 부작용 몰아넣기
  // todo 테스트 코드 작성 - 코어
  @Bean
  public RouterFunction<?> routeSign(SignHandler signHandler) {
    return route()
        .POST("/api/sign", accept(MediaType.APPLICATION_JSON), signHandler::sign)
        .build();
  }
}
