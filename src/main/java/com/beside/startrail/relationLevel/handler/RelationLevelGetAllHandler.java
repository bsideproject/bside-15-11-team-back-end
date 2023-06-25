package com.beside.startrail.relationLevel.handler;

import com.beside.startrail.common.handler.AbstractSignedHandler;
import com.beside.startrail.relationLevel.service.RelationLevelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class RelationLevelGetAllHandler extends AbstractSignedHandler {

    private final RelationLevelService relationLevelService;

    public RelationLevelGetAllHandler(@Value("${sign.attributeName}") String attributeName,
                                      RelationLevelService relationLevelService) {
        super(attributeName);
        this.relationLevelService = relationLevelService;
    }


    @Override
    protected Mono<ServerResponse> signedHandle(ServerRequest serverRequest) {
        return relationLevelService.getRelationLevels()
                .collectList()
                .flatMap(relationLevelProtos ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(relationLevelProtos)
                );
    }
}
