package com.beside.mamgwanboo.relationship.configuration;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.beside.mamgwanboo.relationship.handler.RelationshipCountHandler;
import com.beside.mamgwanboo.relationship.handler.RelationshipGetByFriendSequencehandler;
import com.beside.mamgwanboo.relationship.handler.RelationshipGetBySequenceHandler;
import com.beside.mamgwanboo.relationship.handler.RelationshipPutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

@Configuration
public class RelationshipRouterConfiguration {
  @Bean
  public RouterFunction<?> routeRelationship(
      RelationshipCountHandler relationshipCountHandler,
      RelationshipGetByFriendSequencehandler relationshipGetByFriendSequencehandler,
      RelationshipGetBySequenceHandler relationshipGetBySequenceHandler,
      RelationshipPutHandler relationshipPutHandler
  ) {
    return route()
        .GET("api/relationships/count", relationshipCountHandler)
        .GET("api/relationships/", relationshipGetByFriendSequencehandler)
        .GET("api/relationships/{sequence}", relationshipGetBySequenceHandler)
        .PUT("api/relationships", relationshipPutHandler)
        .build();
  }
}
