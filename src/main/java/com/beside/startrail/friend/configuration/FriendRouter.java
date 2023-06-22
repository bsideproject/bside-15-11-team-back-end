package com.beside.startrail.friend.configuration;

import com.beside.startrail.friend.handler.FriendHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

@Configuration
public class FriendRouter {

    private final FriendHandler friendHandler;

    public FriendRouter(FriendHandler friendHandler) {
        this.friendHandler = friendHandler;
    }

    @Bean
    public RouterFunction<?> routerFriend(){
        return RouterFunctions.route()
                .GET("/api/friend/{sequence}", friendHandler::getFriendBySequence)
                .GET("/api/friend", friendHandler::getFriendsByCriteria)
                .POST("/api/friend",friendHandler::createFriend)
                .PUT("/api/friend/{sequence}", friendHandler::updateFriend)
                .DELETE("/api/friend/{sequence}", friendHandler::removeFriend)
                .build();
    }
}
