package com.beside.startrail.user.configuration;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.beside.startrail.user.handler.UserCreateHandler;
import com.beside.startrail.user.handler.UserGetHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

@Configuration
public class UserRouterConfiguration {
  @Bean
  public RouterFunction<?> routeUser(
      UserGetHandler userGetHandler,
      UserCreateHandler userCreateHandler
  ) {
    return route()
        .GET("/api/users", userGetHandler)
        .PATCH("/api/users", userCreateHandler)
        .build();
  }
}
