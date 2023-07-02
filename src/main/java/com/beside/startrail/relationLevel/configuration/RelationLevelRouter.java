package com.beside.startrail.relationLevel.configuration;

import com.beside.startrail.relationLevel.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

@Configuration
public class RelationLevelRouter {

    private final RelationLevelGetBySequenceHandler relationLevelGetBySequenceHandler;
    private final RelationLevelGetAllByCriteriaHandler relationLevelGetAllByCriteriaHandler;

    private final RelationLevelPostHandler relationLevelPostHandler;
    private final RelationLevelPutHandler relationLevelPutHandler;
    private final RelationLevelDeleteHandler relationLevelDeleteHandler;

    public RelationLevelRouter(RelationLevelGetBySequenceHandler relationLevelGetBySequenceHandler,
                               RelationLevelGetAllByCriteriaHandler relationLevelGetAllByCriteriaHandler,
                               RelationLevelPostHandler relationLevelPostHandler,
                               RelationLevelPutHandler relationLevelPutHandler,
                               RelationLevelDeleteHandler relationLevelDeleteHandler) {
        this.relationLevelGetBySequenceHandler = relationLevelGetBySequenceHandler;
        this.relationLevelGetAllByCriteriaHandler = relationLevelGetAllByCriteriaHandler;
        this.relationLevelPostHandler = relationLevelPostHandler;
        this.relationLevelPutHandler = relationLevelPutHandler;
        this.relationLevelDeleteHandler = relationLevelDeleteHandler;
    }

    @Bean
    public RouterFunction<?> routerRelationLevel(){
        return RouterFunctions.route()
                .GET("/api/relation-level/{sequence}", relationLevelGetBySequenceHandler)
                .GET("/api/relation-level", relationLevelGetAllByCriteriaHandler)
                .POST("/api/relation-level", relationLevelPostHandler)
                .PUT("/api/relation-level/{sequence}", relationLevelPutHandler)
                .DELETE("/api/relation-level/{sequence}", relationLevelDeleteHandler)
                .build();
    }
}
