package com.beside.mamgwanboo.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

@Configuration
@RequiredArgsConstructor
public class FriendRouter {

    private final FriendHandler friendHandler;

    @Bean
    public RouterFunction<?> routerFriend(){
        return RouterFunctions.route()
                .GET("/api/friend", friendHandler::getFriends)
                .POST("/api/friend",friendHandler::createFriend)
                .GET("/api/friend/{sequence}", friendHandler::getFriend)
                .PUT("/api/friend/{sequence}", friendHandler::updateFriend)
                .DELETE("/api/friend/{sequence}", friendHandler::deleteFriend)
                .build();
    }
}
