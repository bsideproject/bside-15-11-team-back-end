package com.beside.startrail.friend.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.friend.service.FriendService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class FriendGetBySequenceHandler extends AbstractSignedHandler {
    private final FriendService friendService;
    private final FriendRequestValidator friendValidator;

    public FriendGetBySequenceHandler(@Value("${sign.attributeName}") String attributeName,
                               FriendService friendService,
                               FriendRequestValidator friendRequestValidator) {
        super(attributeName);
        this.friendService = friendService;
        this.friendValidator = friendRequestValidator;
    }

    @Override
    protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
        String sequence = serverRequest.pathVariable("sequence");

        return friendService.getFriendBySequence(super.jwtPayloadProto.getSequence(), sequence)
                .log()
                .flatMap(friend ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(friend)
                );
    }
}
