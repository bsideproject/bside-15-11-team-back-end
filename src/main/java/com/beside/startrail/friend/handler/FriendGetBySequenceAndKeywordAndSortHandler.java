package com.beside.startrail.friend.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.friend.service.FriendService;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.friend.FriendGetRequestProto;
import reactor.core.publisher.Mono;

@Component
public class FriendGetBySequenceAndKeywordAndSortHandler extends AbstractSignedHandler {
    private final FriendService friendService;

    public FriendGetBySequenceAndKeywordAndSortHandler(
            @Value("${sign.attributeName}") String attributeName,
            FriendService friendService
    ) {
        super(attributeName);
        this.friendService = friendService;
    }


    @Override
    protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
        return makeCriteriaParams(serverRequest)
                .flatMapMany(friendGetRequestProto ->
                        friendService.getFriends(
                                super.jwtPayloadProto.getSequence(),
                                friendGetRequestProto.getKeyword(),
                                friendGetRequestProto.getSort()
                        )
                )
                .map(ProtocolBufferUtil::print)
                .collectList()
                .flatMap(body ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(body)
                );
    }

    private Mono<FriendGetRequestProto> makeCriteriaParams(ServerRequest serverRequest) {
        FriendGetRequestProto.Builder builder = FriendGetRequestProto
                .newBuilder()
                .clear();

        serverRequest.queryParams()
                .forEach((key, value) -> {
                    if (Objects.nonNull(FriendGetRequestProto.getDescriptor().findFieldByName(key))) {
                        builder.setField(
                                FriendGetRequestProto.getDescriptor().findFieldByName(key),
                                value.stream().findFirst().orElse("")
                        );
                    }
                });

        return Mono.just(builder.build());
    }
}
