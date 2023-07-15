package com.beside.startrail.friend.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.friend.service.FriendService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class FriendDeleteHandler extends AbstractSignedTransactionalHandler {
  private final FriendService friendService;

  public FriendDeleteHandler(
      @Value("${sign.attributeName}") String attributeName,
      FriendService friendService
  ) {
    super(attributeName);
    this.friendService = friendService;
  }


  @Override
  protected Mono<ServerResponse> signedTransactionalHandle(ServerRequest serverRequest) {
    String sequence = serverRequest.pathVariable("sequence");

    return friendService.removeFriend(super.jwtPayloadProto.getSequence(), sequence)
        .flatMap(friendResponseProto ->
            ServerResponse
                .ok()
                .build()
        );
  }
}
