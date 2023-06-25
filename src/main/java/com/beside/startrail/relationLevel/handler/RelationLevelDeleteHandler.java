package com.beside.startrail.relationLevel.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.relationLevel.service.RelationLevelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import protobuf.common.RelationLevelProto;
import reactor.core.publisher.Mono;

@Component
public class RelationLevelDeleteHandler extends AbstractSignedHandler {

    private final RelationLevelService relationLevelService;

    public RelationLevelDeleteHandler(@Value("${sign.attributeName}") String attributeName,
                                      RelationLevelService relationLevelService) {
        super(attributeName);
        this.relationLevelService = relationLevelService;
    }

    @Override
    protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
        String sequence = serverRequest.pathVariable("sequence");

        return relationLevelService.removeRelationLevel(sequence)
                .flatMap(relationLevelProto ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(isDeleted(relationLevelProto))
                );
    }

    private Boolean isDeleted(RelationLevelProto relationLevelProto){
        return !ObjectUtils.isEmpty(relationLevelProto.getSequence())
                ? Boolean.TRUE
                : Boolean.FALSE;
    }
}
