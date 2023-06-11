package com.beside.mamgwanboo.common.configuration;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.net.URI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class IndexRouterConfiguration {
  @Bean
  RouterFunction<ServerResponse> redirectToIndex() {
    return route()
        .GET(
            "/",
            serverRequest -> ServerResponse.temporaryRedirect(URI.create("/index.html")).build()
        )
        .build();
  }
}
