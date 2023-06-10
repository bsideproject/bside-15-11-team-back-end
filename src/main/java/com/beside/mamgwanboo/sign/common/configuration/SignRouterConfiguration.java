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
  @Bean
  public RouterFunction<?> routeSign(SignHandler signHandler) {
    return route()
        .POST(
            "/api/sign",
            accept(MediaType.APPLICATION_JSON),
            signHandler
        )
        .build();
  }
}
