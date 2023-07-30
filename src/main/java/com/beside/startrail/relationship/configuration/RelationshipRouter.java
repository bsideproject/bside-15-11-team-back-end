package com.beside.startrail.relationship.configuration;

import com.beside.startrail.relationship.handler.RelationshipDeleteHandler;
import com.beside.startrail.relationship.handler.RelationshipGetBySequenceAndKeywordAndSortHandler;
import com.beside.startrail.relationship.handler.RelationshipGetBySequenceHandler;
import com.beside.startrail.relationship.handler.RelationshipPostHandler;
import com.beside.startrail.relationship.handler.RelationshipPutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

@Configuration
public class RelationshipRouter {
  private final RelationshipPostHandler relationshipPostHandler;
  private final RelationshipPutHandler relationshipPutHandler;
  private final RelationshipDeleteHandler relationshipDeleteHandler;
  private final RelationshipGetBySequenceHandler relationshipGetBySequenceHandler;
  private final RelationshipGetBySequenceAndKeywordAndSortHandler
      relationshipGetBySequenceAndKeywordAndSortHandler;

  public RelationshipRouter(
      RelationshipGetBySequenceHandler relationshipGetBySequenceHandler,
      RelationshipGetBySequenceAndKeywordAndSortHandler relationshipGetBySequenceAndKeywordAndSortHandler,
      RelationshipPostHandler relationshipPostHandler,
      RelationshipPutHandler relationshipPutHandler,
      RelationshipDeleteHandler relationshipDeleteHandler
  ) {
    this.relationshipGetBySequenceHandler = relationshipGetBySequenceHandler;
    this.relationshipGetBySequenceAndKeywordAndSortHandler = relationshipGetBySequenceAndKeywordAndSortHandler;
    this.relationshipPostHandler = relationshipPostHandler;
    this.relationshipPutHandler = relationshipPutHandler;
    this.relationshipDeleteHandler = relationshipDeleteHandler;
  }

  @Bean
  public RouterFunction<?> routerRelationship() {
    return RouterFunctions.route()
        .GET("/api/relationships/{sequence}", relationshipGetBySequenceHandler)
        .GET("/api/relationships", relationshipGetBySequenceAndKeywordAndSortHandler)
        .POST("/api/relationships", relationshipPostHandler)
        .PUT("/api/relationships/{sequence}", relationshipPutHandler)
        .DELETE("/api/relationships/{sequence}", relationshipDeleteHandler)
        .build();
  }
}
