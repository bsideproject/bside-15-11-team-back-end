package com.beside.startrail.friend.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.friend.service.FriendService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.friend.FriendResponseProto;
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
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(isDeleted(friendResponseProto))
        );
  }

  private Boolean isDeleted(FriendResponseProto friendResponseProto) {
    return !ObjectUtils.isEmpty(friendResponseProto.getSequence());
  }
}
