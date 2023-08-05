package com.beside.startrail.sign.configuration;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.beside.startrail.sign.handler.SignHandler;
import com.beside.startrail.sign.handler.SignWithdrawalHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

@Configuration
public class SignRouterConfiguration {
  @Bean
  public RouterFunction<?> routeSign(
      SignHandler signHandler,
      SignWithdrawalHandler signWithdrawalHandler
  ) {
    return route()
        .POST("/api/sign", signHandler)
        .POST("/api/sign/withdrawal", signWithdrawalHandler)
        .build();
  }
}

