package com.beside.startrail.friend.configuration;

import com.beside.startrail.friend.handler.FriendDeleteHandler;
import com.beside.startrail.friend.handler.FriendGetByCriteriaHandler;
import com.beside.startrail.friend.handler.FriendGetBySequenceHandler;
import com.beside.startrail.friend.handler.FriendPostHandler;
import com.beside.startrail.friend.handler.FriendPutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

@Configuration
public class FriendRouter {
  private final FriendPostHandler friendPostHandler;
  private final FriendPutHandler friendPutHandler;
  private final FriendDeleteHandler friendDeleteHandler;
  private final FriendGetBySequenceHandler friendGetBySequenceHandler;
  private final FriendGetByCriteriaHandler friendGetByCriteria;

  public FriendRouter(
      FriendGetBySequenceHandler friendGetBySequenceHandler,
      FriendGetByCriteriaHandler friendGetByCriteria,
      FriendPostHandler friendPostHandler,
      FriendPutHandler friendPutHandler,
      FriendDeleteHandler friendDeleteHandler
  ) {
    this.friendGetBySequenceHandler = friendGetBySequenceHandler;
    this.friendGetByCriteria = friendGetByCriteria;
    this.friendPostHandler = friendPostHandler;
    this.friendPutHandler = friendPutHandler;
    this.friendDeleteHandler = friendDeleteHandler;
  }

  @Bean
  public RouterFunction<?> routerFriend() {
    return RouterFunctions.route()
        .GET("/api/friend/{sequence}", friendGetBySequenceHandler)
        .GET("/api/friend", friendGetByCriteria)
        .POST("/api/friend", friendPostHandler)
        .PUT("/api/friend/{sequence}", friendPutHandler)
        .DELETE("/api/friend/{sequence}", friendDeleteHandler)
        .build();
  }
}
