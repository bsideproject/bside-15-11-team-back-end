package com.beside.startrail.friend.handler;

import com.beside.startrail.common.handler.AbstractSignedTransactionalHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.friend.service.FriendService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.friend.FriendPutProto;
import reactor.core.publisher.Mono;

@Component
public class FriendPutHandler extends AbstractSignedTransactionalHandler {
    private final FriendService friendService;
    private final FriendRequestValidator friendValidator;

    public FriendPutHandler(@Value("${sign.attributeName}") String attributeName,
                            FriendService friendService,
                            FriendRequestValidator friendRequestValidator) {
        super(attributeName);
        this.friendService = friendService;
        this.friendValidator = friendRequestValidator;
    }

    @Override
    protected Mono<ServerResponse> signedTransactionalHandle(ServerRequest serverRequest) {
        String sequence = serverRequest.pathVariable("sequence");

        return serverRequest.bodyToMono(String.class)
                .flatMap(body -> ProtocolBufferUtil.<FriendPutProto>parse(body, FriendPutProto.newBuilder()))
                .log()
                .doOnNext(friendValidator::updateValidate)
                .flatMap(body -> friendService.updateFriend(super.jwtPayloadProto.getSequence(), sequence, body))
                .flatMap(friendDto ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(friendDto)
                );
    }
}
