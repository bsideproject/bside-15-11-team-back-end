package com.beside.startrail.user.configuration;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.beside.startrail.user.handler.UserExistsHandler;
import com.beside.startrail.user.handler.UserGetHandler;
import com.beside.startrail.user.handler.UserPatchHandler;
import com.beside.startrail.user.handler.UserPostHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

@Configuration
public class UserRouterConfiguration {
  @Bean
  public RouterFunction<?> routeUser(
      UserExistsHandler userExistsHandler,
      UserGetHandler userGetHandler,
      UserPostHandler userPostHandler,
      UserPatchHandler userPatchHandler
  ) {
    return route()
        .GET("/api/users/{oauthServiceType}/{serviceUserId}", userExistsHandler)
        .GET("/api/users", userGetHandler)
        .POST("/api/users", userPostHandler)
        .PATCH("/api/users", userPatchHandler)
        .build();
  }
  // user protobuf
}
