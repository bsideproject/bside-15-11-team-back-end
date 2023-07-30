package com.beside.startrail.mind.configuration;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.beside.startrail.mind.handler.MindCountHandler;
import com.beside.startrail.mind.handler.MindDeleteHandler;
import com.beside.startrail.mind.handler.MindGetByRelationshipSequencehandler;
import com.beside.startrail.mind.handler.MindGetBySequenceHandler;
import com.beside.startrail.mind.handler.MindPostHandler;
import com.beside.startrail.mind.handler.MindPutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

@Configuration
public class MindRouterConfiguration {
  @Bean
  public RouterFunction<?> routeMind(
      MindCountHandler mindCountHandler,
      MindGetByRelationshipSequencehandler mindGetByRelationshipSequencehandler,
      MindGetBySequenceHandler mindGetBySequenceHandler,
      MindPostHandler mindPostHandler,
      MindPutHandler mindPutHandler,
      MindDeleteHandler mindDeleteHandler
  ) {
    return route()
        .GET("/api/minds/count", mindCountHandler)
        .GET("/api/minds", mindGetByRelationshipSequencehandler)
        .GET("/api/minds/{sequence}", mindGetBySequenceHandler)
        .POST("/api/minds", mindPostHandler)
        .PUT("/api/minds", mindPutHandler)
        .DELETE("/api/minds/{sequence}", mindDeleteHandler)
        .build();
  }
}
