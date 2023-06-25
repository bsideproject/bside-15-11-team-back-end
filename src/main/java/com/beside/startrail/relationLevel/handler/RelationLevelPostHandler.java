package com.beside.startrail.relationLevel.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.common.protocolbuffer.ProtocolBufferUtil;
import com.beside.startrail.relationLevel.service.RelationLevelService;
import com.mongodb.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.common.RelationLevelProto;
import reactor.core.publisher.Mono;

@Component
public class RelationLevelPostHandler extends AbstractSignedHandler {

    private final RelationLevelService relationLevelService;

    public RelationLevelPostHandler(@Value("${sign.attributeName}") String attributeName,
                                    RelationLevelService relationLevelService) {
        super(attributeName);
        this.relationLevelService = relationLevelService;
    }

    @Override
    protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(String.class)
                .flatMap(body -> ProtocolBufferUtil.<RelationLevelProto>parse(body, RelationLevelProto.newBuilder()))
                //.doOnNext()
                .flatMap(relationLevelService::createRelationLevel)
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> new IllegalArgumentException("Duplicate key, RelationLevel sequence")
                )
                .flatMap(relationLevelProto ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(relationLevelProto)
                );
    }
}
