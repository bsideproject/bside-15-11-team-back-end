package com.beside.startrail.friend.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.friend.service.FriendService;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.friend.FriendPostRequestProto;
import reactor.core.publisher.Mono;

@Component
public class FriendPostHandler extends AbstractSignedTransactionalHandler {
  private final FriendService friendService;
  private final FriendRequestValidator friendValidator;

  public FriendPostHandler(
      @Value("${sign.attributeName}") String attributeName,
      FriendService friendService,
      FriendRequestValidator friendRequestValidator
  ) {
    super(attributeName);
    this.friendService = friendService;
    this.friendValidator = friendRequestValidator;
  }

  @Override
  protected Mono<ServerResponse> signedTransactionalHandle(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(String.class)
        .flatMap(body -> ProtocolBufferUtil.<FriendPostRequestProto>parse(
                body, FriendPostRequestProto.newBuilder()
            )
        )
        .doOnNext(friendValidator::createValidate)
        .flatMap(friendDto -> friendService.createFriend(
                super.jwtPayloadProto.getSequence(),
                friendDto
            )
        )
        .map(friendResponseProtos ->
            friendResponseProtos.stream()
                .map(ProtocolBufferUtil::print)
                .collect(Collectors.toList())
        )
        .flatMap(body ->
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
        );
  }
}
