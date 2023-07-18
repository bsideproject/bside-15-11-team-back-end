package com.beside.startrail.friend.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.friend.service.FriendService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.friend.FriendGetCriteriaProto;
import reactor.core.publisher.Mono;

@Component
public class FriendGetByCriteriaHandler extends AbstractSignedHandler {
  private final FriendService friendService;

  public FriendGetByCriteriaHandler(
      @Value("${sign.attributeName}") String attributeName,
      FriendService friendService
  ) {
    super(attributeName);
    this.friendService = friendService;
  }


  @Override
  protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
    return makeCriteriaParams(serverRequest)
        .flatMapMany(friendSearchCriteria ->
            friendService.getFriendsByCriteria(
                super.jwtPayloadProto.getSequence(),
                friendSearchCriteria
            )
        )
        .collectList()
        .flatMap(friendDtoList ->
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(friendDtoList)
        );
  }

  private Mono<FriendGetCriteriaProto> makeCriteriaParams(ServerRequest request) {
    FriendGetCriteriaProto.Builder friendSearchCriteriaBuilder = FriendGetCriteriaProto
        .newBuilder()
        .clear();

    request.queryParams()
        .forEach((key, value) -> {
          if (!ObjectUtils.isEmpty(FriendGetCriteriaProto.getDescriptor().findFieldByName(key))) {
            friendSearchCriteriaBuilder.setField(
                FriendGetCriteriaProto.getDescriptor().findFieldByName(key),
                value.stream().findFirst().orElse("")
            );
          }
        });

    return Mono.just(friendSearchCriteriaBuilder.build());
  }
}
